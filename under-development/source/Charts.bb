;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: CHARTS -------------------------
;//////////////////////////////////////////////////////////////////////////////

;------------------------------------------------------------------------------
;//////////////////////////// 28. GAME CHART //////////////////////////////////
;------------------------------------------------------------------------------
Function GameChart()
;update charts
UpdateCharts(0)
;box thumbnails
For cyc=1 To 10
 chartBox(chartList(cyc))=LoadImage("Games/Box Art/Box"+Dig$(chartGame(chartList(cyc)),100)+".jpg")
 If chartBox(chartList(cyc))=0 Then chartBox(chartList(cyc))=LoadImage("Games/Box Art/Box000.jpg")
 ResizeImage chartBox(chartList(cyc)),26,36
 MaskImage chartBox(chartList(cyc)),255,0,255
Next
;magazine
eNewspaper(1)=1
eAdvert(1)=Rnd(8,14) : negFormat=-1
If eAdvert(1)=<9 Then eAdvert(1)=Rnd(1,9) 
;frame ratings
timer=CreateTimer(30)
;MAIN LOOP
go=0 : gotim=-10 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 
	 If KeyDown(1) Or MouseDown(1) 
	  If keytim=0 And gotim<chartTim(1) Then gotim=gotim+20 : keytim=5 ;: PlaySound sMenuSelect 
	  If gotim>chartTim(1)+40 And keytim=0 Then go=1
	 EndIf
	EndIf
	;reactions
	For count=1 To 10
	  If gotim=chartTim(count) 
	   PlaySound sMenuSelect
	   If count>3
	    If chartChar(chartList(count))=gamChar
	     If chartRank(chartList(count))=<chartOldRank(chartList(count))
	      ProduceSound(0,sCheer,0,0.5) ;player climbs
	     Else
	      ProduceSound(0,sGrumble,0,0.25) ;player falls
	     EndIf
	    Else
	     If chartOldRank(chartList(count))=0 Then ProduceSound(0,sCheer,0,0.25) ;new entry
	    EndIf
	   EndIf 
	   ;If count=3 Then ProduceSound(0,sPopReact(3),0,0.5) ;#3
	   ;If count=2 Then ProduceSound(0,sPopReact(4),0,0.5) ;#2
	   If count=1 Then PlaySound sCheer ;#1
	  EndIf
	Next
	;paper impact
	If gotim=0 Then ProduceSound(0,sPaper,22050,0.25)
	;manage music
	ManageMusic(0) 
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;magazine
 If gotim>0 
  x=rX#(400) : y=GraphicsHeight()-294
  DisplayMagazine(1,x,y)
 EndIf
 ;rankings
 x=rX#(400) : y=GraphicsHeight()-50;rY#(550)
 For count=1 To 10
  cyc=chartList(11-count)
  If gotim>chartTim(11-count) 
   ;photos
   DrawImage chartBox(cyc),x-250,y
   reveal=(64-PortraitHead#(chartChar(cyc)))+18
   DrawImageRect charPhoto(chartChar(cyc)),x-275,(y+18)+(64-reveal),0,0,76,reveal
   ;rank
   SetFont fontNews(7)
   If 11-count=1 Then SetFont fontNews(10) 
   If 11-count=>10 Then SetFont fontNews(5) 
   If chartChar(cyc)=gamChar Then r=255 : g=Rnd(175,225) : b=0 Else r=255 : g=240 : b=0
   Outline(11-count+".",x-220,y,0,0,0,r,g,b)
   ;game
   namer$=softTitle$(chartGame(cyc))
   If softDated(chartGame(cyc))>0 Then namer$=namer$+" "+GetYear(gamDate)
   SqueezeNewsFont(namer$,200,24)
   If chartChar(cyc)=gamChar Then r=255 : g=Rnd(175,225) : b=0 Else r=0 : g=0 : b=0
   OutlineStraight(namer$,x-201,y-2,0,0,0,r,g,b)
   ;credit
   SetFont fontNews(1)
   If chartChar(cyc)=gamChar Then r=255 : g=Rnd(175,225) : b=0 Else r=100 : g=100 : b=100
   OutlineStraight("("+charName$(chartChar(cyc))+", "+fedName$(chartCompany(cyc))+")",x-198,y+14,100,100,100,r,g,b)
   ;format
   SetFont fontNews(3)
   If chartChar(cyc)=gamChar Then r=255 : g=Rnd(175,225) : b=0 Else r=150 : g=100 : b=150
   Outline(hardTitle$(chartFormat(cyc)),x+85,y,150,100,150,r,g,b)
   ;sales
   SetFont fontNews(2)
   If chartChar(cyc)=gamChar Then r=255 : g=Rnd(175,225) : b=0 Else r=130 : g=230 : b=130
   Outline(GetFigure$(chartSales(cyc)),x+220,y-2,25,50,25,r,g,b)
   SetFont fontNews(0)
   Outline("copies sold",x+220,y+11,25,50,25,25,50,25)
   ;progress
   If chartOldRank(cyc)>0 
    If chartRank(cyc)=chartOldRank(cyc) Then DrawImage gChartStatic,x+290,y
    If chartRank(cyc)<chartOldRank(cyc) Then DrawImage gChartUp,x+290,y
    If chartRank(cyc)>chartOldRank(cyc) Then DrawImage gChartDown,x+290,y 
   Else 
    DrawImage gChartNew,x+290,y
   EndIf
  EndIf
  y=y-38
 Next 
 ;prompt 
 If gotim>chartTim(1)+40
  SetFont fontNews(2)
  Outline(">>> CLICK TO PROCEED >>>",rX#(405),GraphicsHeight()-15,100,100,100,255,255,255)
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed to finances
PlaySound sMenuGo
FreeTimer timer
For cyc=1 To 10
 FreeImage chartBox(cyc)
Next
screen=24
End Function

;//////////////////////////////////////////////////////////////////////////////
;--------------------------- 24. FINANCIAL REPORT -----------------------------
;//////////////////////////////////////////////////////////////////////////////
Function FinanceReport()
;reset working figures
fed=charFed(gamChar)
For count=1 To 8
 gamFinances(count,0)=0
 gamFinances(count,1)=0
Next
;calculate income
If gamRelease>0
 gamFinances(1,1)=gamSales
 If gamSales<1000 Then chartPromotion#(1)=0
 margin=hardRetail(projFormat(gamRelease))-formatCost(hardFormat(projFormat(gamRelease)))
 gamFinances(2,1)=gamSales*margin
 gamFinances(3,1)=PercentOf#(gamFinances(2,1),charRoyalties(gamChar))
EndIf
;calculate expenses
If gamFinances(3,1)>0
 gamFinances(4,1)=PercentOf#(gamFinances(3,1),gamTax)
Else 
 If gamBank>0 Then gamFinances(4,1)=PercentOf#(gamBank,1)
EndIf
gamFinances(5,1)=CountDesks()*gamRent
If fedPolicy(charFed(gamChar))=9 Then gamFinances(5,1)=0
gamFinances(6,1)=CalculateWages()
;calculate profit
gamFinances(7,1)=gamFinances(3,1)-(gamFinances(4,1)+gamFinances(5,1)+gamFinances(6,1))
oldBank=gamBank
gamFinances(8,0)=gamBank
gamFinances(8,1)=gamBank+gamFinances(7,1)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=0
go=0 : gotim=-20 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PROCESS
    gotim=gotim+1
	;initial subject
	If gotim>20 And foc=0
	 PlaySound sMenuSelect
	 If gamRelease>0 Then foc=1 Else foc=4
	EndIf
	If gotim>30 And keytim=0
	 ;develop figures
	 For cyc=1 To 8
	  If foc=cyc And keytim=0
	   If gamFinances(cyc,0)<>gamFinances(cyc,1) 
	    ProduceSound(0,sMenuBrowse,0,0.25) : keytim=1
	    gamFinances(cyc,0)=gamFinances(cyc,0)+PursueFigure(gamFinances(cyc,0),gamFinances(cyc,1))
	   EndIf
	   If gamFinances(cyc,0)=gamFinances(cyc,1) 
	    foc=foc+1 : keytim=20
	    If foc>8 Then vol#=1.0 Else vol#=0.5
	    If gamFinances(cyc,1)=0 Then PlaySound sMenuSelect Else ProduceSound(0,sCash,0,vol#)
	    If foc>8 Then gamBank=gamFinances(8,1)
	   EndIf
	  EndIf
	 Next
	 ;leave
	 If foc>8 And keytim=0
	  If KeyDown(1) Or KeyDown(28) Or MouseDown(1) Then go=1
	 EndIf
	EndIf
	;speed up
	If foc=>1 And foc=<8 And keytim>0
	 If KeyDown(1) Or KeyDown(28) Or MouseDown(1) Then keytim=keytim/2
	EndIf
	;paper thud
	If gotim=0 Then ProduceSound(0,sPaper,22050,0.25)
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 If gotim>0
  x=rX#(400) : y=GraphicsHeight()-284
  DrawImage gReport,x,y
  ;smallprint
  DrawImage gLabel(fed),x+200,y-164
  SetFont fontNews(3)
  OutlineStraight(charName$(gamChar),x-70,y-190,100,100,100,100,100,100)
  SetFont fontNews(1)
  OutlineStraight(DescribeDate$(gamDate,1),x-75,y-127,100,100,100,100,100,100)
  If gamRelease>0 And projBox(gamRelease)>0
   DrawImage projBox(gamRelease),x-193,y-12
   If projPirated(gamRelease)>0 Then DrawImage gPiracy,x-193,y-12
  EndIf
  ;items
  For cyc=1 To 8
   ;positioning
   showX=x+20
   If cyc=1 Then showY=y-71
   If cyc=2 Then showY=y-36
   If cyc=3 Then showY=y-1
   If cyc=4 Then showY=y+53
   If cyc=5 Then showY=y+88
   If cyc=6 Then showY=y+123
   If cyc=7 Then showY=y+179
   If cyc=8 Then showY=y+214
   Color 175,175,175
   Line showX-140,y+26,showX+140,y+26
   Line showX-140,y+149,showX+140,y+149
   ;headers
   If cyc=1
    header$="SALES:"
    If gamRelease>0
     sum$=GetFigure$(gamFinances(cyc,0))+" x $"+hardRetail(projFormat(gamRelease))
     If formatCost(hardFormat(projFormat(gamRelease)))>0
      sum$=sum$+" (-$"+formatCost(hardFormat(projFormat(gamRelease)))+")"
     EndIf
    Else
     sum$="$0"
    EndIf
   Else
    sum$="$"+GetFigure$(gamFinances(cyc,0))
    If cyc=2 Then header$="REVENUE:"
    If cyc=3 Then header$=charRoyalties(gamChar)+"% ROYALTIES:"
    If cyc=4
     If gamRelease>0 Then header$=gamTax+"% TAX:" Else header$="SAVINGS TAX:"
    EndIf
    If cyc=5 Then header$="RENT:"
    If cyc=6 Then header$="WAGES:"
    If cyc=7 Then header$="PROFIT:"
    If cyc=8 Then header$="BALANCE:"
   EndIf
   ;highlighter
   SetFont fontNews(2)
   If foc>0 And foc=cyc
    Color 150,150,150
    Rect (showX-5)-StringWidth(header$),showY-7,(StringWidth(header$)+5)+(StringWidth(sum$)+5),16,0
    Color 0,0,0
    Rect (showX-7)-StringWidth(header$),showY-9,(StringWidth(header$)+5)+(StringWidth(sum$)+9),20,0
   EndIf
   ;display text
   OutlineStraight(header$,(showX-3)-StringWidth(header$),showY,225,220,215,50,50,50)
   r=50 : g=50 : b=50
   If gamFinances(cyc,0)>0 Then r=0 : g=150 : b=0
   If gamFinances(cyc,0)<0 Or (cyc=>4 And cyc=<6 And gamFinances(cyc,0)>0) Then r=150 : g=0 : b=0
   If cyc=8
    r=50 : g=50 : b=50
    If gamFinances(cyc,0)>oldBank Then r=0 : g=150 : b=0
    If gamFinances(cyc,0)<oldBank Then r=150 : g=0 : b=0
   EndIf
   OutlineStraight(sum$,showX+3,showY,225,220,215,r,g,b)
   ;explanations
   If foc>cyc
    explain$=""
    stringer=StringWidth(sum$)
    SetFont fontNews(0) 
    OutlineStraight(explain$,(showX+3)+(stringer+5),showY,225,220,215,100,100,100)
   EndIf
  Next
  ;prompt
  If foc>8 And gotim>50
   SetFont fontNews(2)
   Outline(">>> CLICK TO PROCEED >>>",x+20,GraphicsHeight()-25,100,100,100,255,255,255)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=-1 Then PlaySound sMenuBack Else PlaySound sMenuGo
FreeTimer timer
;save progress
Loader("Please Wait","Saving Progress")
SaveUniverse()
Loader("","") : SaveProgress(slot)
Loader("","") : SaveWorld(slot)
Loader("","") : SaveChars(slot)
;find meetings
RiskContractOffers()
RiskCompanyMeetings()
RiskCollaborations()
RiskTeamMeetings()
If screen<>25 And screen<>29 Then screen=20
End Function

;----------------------------------------------------------------------
;///////////////////// 25. RETIREMENT SUMMARY /////////////////////////
;----------------------------------------------------------------------
Function Retirement()
;get working values
hiWealth(0)=gamBank
hiRep(0)=charRep(gamChar)
hiExperience(0)=CountExperience(gamChar,0)
;newpaper identity
eNewspaper(1)=1
eAdvert(1)=Rnd(8,14) : negFormat=-1
If eAdvert(1)=<9 Then eAdvert(1)=Rnd(1,9) 
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
go=0 : gotim=-10 : keytim=0
While go=0
 
 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;PORTAL
    gotim=gotim+1
	If gotim>100 
	 If KeyDown(1) Or MouseDown(1) Then go=1
	EndIf 
	;reaction
	If gotim=0 
	 ProduceSound(0,sPaper,22050,0.25)
	 If hiWealth(0)>100000 And hiRep(0)>50 Then PlaySound sCheer Else PlaySound sGrumble
	EndIf
	;manage music
	ManageMusic(0) 
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)  
 ;CONSTRUCT NEWSPAPER
 If gotim>0
  ;base image
  x=rX#(400) : y=GraphicsHeight()-294
  DisplayMagazine(1,x,y)
  ;career summary
  SetFont fontNews(5) : Color 0,0,0
  newsX=x+5 : newsY=y+200 : spacing=40
  g=GetGender(gamChar)
  Text newsX,newsY-PercentOf#(spacing,80),"After "+GetFigure$(hiExperience(0))+" "+textPeriod$(optTime)+"s, "+charName$(gamChar)+" has retired from",1,1
  If hiWealth(0)=>0 Then namer$="game development with a fortune of $"+GetFigure$(hiWealth(0))+"."
  If hiWealth(0)<0 Then namer$="game development with debts of $"+GetFigure$(Abs(hiWealth(0)))+"."
  Text newsX,newsY,namer$,1,1
  namer$="Nobody will remember that "+Lower$(He$(g))+" ever existed."
  If hiRep(0)>50 Then namer$="Diehard fans will continue to cherish "+Lower$(His$(g))+" work."
  If hiRep(0)>75 Then namer$="The games"+Lower$(He$(g))+" made will continue to be enjoyed by many."
  If hiRep(0)>90 Then namer$=He$(g)+" goes down in history as one of the greatest of all time."
  Text newsX,newsY+PercentOf#(spacing,80),namer$,1,1
  ;display headline
  headline$="GAME OVER" 
  SqueezeNewsFont(headline$,670,70)
  Color 0,0,0 : Text newsX,y-80,headline$,1,1
  ;photo display
  photoX=x-140 : photoY=y+60
  DrawImage gNewsScene(1),photoX,photoY
  DrawImage gLabel(charFed(gamChar)),photoX,photoY
  DrawImage charPhoto(gamChar),photoX+120,photoY+5
  DrawCharProfile(gamChar,photoX+260,photoY-86,0)
  ;prompt
  If gotim>100
   SetFont fontNews(2)
   Outline(">>> CLICK TO PROCEED >>>",newsX,GraphicsHeight()-25,100,100,100,255,255,255)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
;Loader("Please Wait","Saving Career")
PlaySound sMenuGo
FreeTimer timer
;find hall of fame slot
hiInduct=0
For cyc=1 To 10
 If hiChar(cyc)=0 Then hiInduct=cyc
Next
If hiInduct=0
 hi=999999999 : loser=0
 For cyc=1 To 10
  If hiWealth(cyc)<hi Then hi=hiWealth(cyc) : loser=cyc
 Next
 hiInduct=loser
EndIf
;transfer details
If hiInduct>0
 hiChar(hiInduct)=gamChar
 hiName$(hiInduct)=charName$(gamChar)
 hiPhoto(hiInduct)=charPhoto(gamChar)
 hiPhotoHeight#(hiInduct)=PortraitHead#(gamChar)
 hiPhotoR(hiInduct)=charPhotoR(gamChar)
 hiPhotoG(hiInduct)=charPhotoG(gamChar)
 hiPhotoB(hiInduct)=charPhotoB(gamChar)
 MaskImage hiPhoto(hiInduct),hiPhotoR(hiInduct),hiPhotoG(hiInduct),hiPhotoB(hiInduct)
 SaveImage(hiPhoto(hiInduct),"Data/Hall Of Fame/Photo"+Dig$(hiInduct,10)+".bmp")
 hiWealth(hiInduct)=hiWealth(0)
 hiRep(hiInduct)=hiRep(0)
 hiExperience(hiInduct)=hiExperience(0)
 hiFed(hiInduct)=charFed(gamChar) 
EndIf
;move to independent pool
;If charFed(gamChar)<no_feds Then MoveChar(gamChar,no_feds)
slotActive(slot)=0
hiChar(0)=0
SaveUniverse()
;proceed
screen=26
End Function

;----------------------------------------------------------------------
;/////////////////////// 26. HALL OF FAME /////////////////////////////
;----------------------------------------------------------------------
Function HallOfFame()
;initiate list
If hiInduct>0 Then PlaySound sCheer
CareerRankings(1)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=3 : oldfoc=foc : page=1
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>30 Then flashTim=0 
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 
	 ;quit
	 If KeyDown(1) Then go=-1 
	 ;proceed
	 If MouseDown(1) And foc=3 Then go=-1
	EndIf
	;manage music
	ManageMusic(0) 
	  
	;CONFIGURE 
	oldPage=page
	If gotim>20 And keytim=0
	 ;change category
	 mousy=0
	 If MouseX()>rX#(100) And MouseX()<rX#(700) And MouseY()>rY#(115) And MouseY()<rY#(525) Then mousy=1
	 If mousy=1 And MouseDown(2) And foc=0 Then page=page-1 : PlaySound sMenuBrowse : keytim=6 
	 If mousy=1 And MouseDown(1) And foc=0 Then page=page+1 : PlaySound sMenuBrowse : keytim=6  
	 ;reset entries
	 If KeyDown(29) And KeyDown(19)
	  Loader("Please Wait","Populating List")
	  PlaySound sTrash : keytim=10 
	  For count=1 To 10
       If count=10 Then hiChar(count)=84 Else hiChar(count)=Rnd(1,no_chars)
       hiName$(count)=charName$(hiChar(count))
       hiPhoto(count)=charPhoto(hiChar(count))
 	   hiPhotoHeight#(count)=PortraitHead#(hiChar(count))
 	   hiPhotoR(count)=charPhotoR(hiChar(count))
 	   hiPhotoG(count)=charPhotoG(hiChar(count))
 	   hiPhotoB(count)=charPhotoB(hiChar(count))
 	   MaskImage hiPhoto(count),hiPhotoR(count),hiPhotoG(count),hiPhotoB(count)
 	   SaveImage(hiPhoto(count),"Data/Hall Of Fame/Photo"+Dig$(count,10)+".bmp")     
       hiWealth(count)=count*1000
       hiRep(count)=count*5
       hiExperience(count)=count
       hiFed(count)=charFed(hiChar(count))
      Next 
      CareerRankings(page)
	 EndIf
	EndIf
	;limits
	If page<1 Then page=3
	If page>3 Then page=1
    ;update list
	If page<>oldPage 
	 CareerRankings(page)
    EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(2),rX#(400),rY#(60)
 ;RANKINGS
 x=165 : y=173
 For count=1 To 10
  cyc=hiRank(count)
  ;photo
  If hiPhoto(cyc)>0
   If count=1
    DrawImage hiPhoto(cyc),rX#(x),rY#(y)-40 
   Else
    reveal=(64-hiPhotoHeight#(cyc))+10
    DrawImageRect hiPhoto(cyc),rX#(x),(rY#(y)+10)+(64-reveal),0,0,76,reveal
   EndIf
  EndIf
  ;position
  SetFont fontNews(5)
  If hiChar(cyc)>0 And count=1 Then SetFont fontNews(9) 
  If hiChar(cyc)>0 And count=10 Then SetFont fontNews(4) 
  r=255 : g=240 : b=0
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(175,250) : b=130
  If hiChar(cyc)>0 Then Outline(count+".",rX#(x+35),rY#(y),0,0,0,r,g,b)
  If hiChar(cyc)=0 Then Outline("-",rX#(x+35),rY#(y),0,0,0,r,g,b)
  ;name
  r=255 : g=255 : b=255 : outR=0 : outG=0 : outB=0
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(175,250) : b=130 : outR=50 : outG=50 : outB=50
  If hiChar(cyc)>0 Then namer$=hiName$(cyc) Else namer$=""
  SqueezeFont(namer$,150,18)
  OutlineStraight(namer$,rX#(x+55),rY#(y),outR,outG,outB,r,g,b)
  ;promotion
  r=85 : g=85 : b=85 : outR=85 : outG=85 : outB=85
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(175,250) : b=130 : outR=50 : outG=50 : outB=50
  If hiChar(cyc)>0 Then namer$=fedName$(hiFed(cyc)) Else namer$=""
  SqueezeFont(namer$,100,15)
  Outline(namer$,rX#(x+305),rY#(y),outR,outG,outB,r,g,b)
  ;value
  SetFont fontNews(4)
  If page=1 Or page=3 Then SetFont fontNews(3)
  If page=1 Then namer$="$"+GetFigure$(hiWealth(cyc)) : header$="Wealthiest Careers:"
  If page=2 Then namer$=hiRep(cyc)+"%" : header$="Most Popular Careers:"
  If page=3 Then namer$=hiExperience(cyc)+" "+textPeriod$(optTime)+"s" : header$="Longest Careers:"
  If hiChar(cyc)=0 Then namer$=""
  r=130 : g=230 : b=130 
  If hiInduct>0 And cyc=hiInduct Then r=255 : g=Rnd(175,250) : b=130
  Outline(namer$,rX#(x+440),rY#(y),25,50,25,r,g,b)
  y=y+37
 Next 
 ;header
 SetFont fontNews(4)
 Outline(header$,rX#(400),rY#(130),50,0,50,200,100,200)
 ;options
 foc=0
 DrawOption(3,rX#(400),rY#(560),"<<< EXIT <<<","")
 ;advice
 ;SetFont font(1)
 ;If foc=1 And flashTim>15
  ;Outline("CLICK TO BROWSE",rX#(80),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(80),rY#(320)+10,0,0,0,255,255,255) 
  ;Outline("CLICK TO BROWSE",rX#(720),rY#(320)-10,0,0,0,255,255,255)
  ;Outline("CATEGORIES",rX#(720),rY#(320)+10,0,0,0,255,255,255) 
 ;EndIf
 ;cursor 
 If foc<>oldfoc
  If foc=3 Then PlaySound sMenuSelect
  oldfoc=foc 
 EndIf
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeTimer timer
screen=10
If hiInduct>0 Then screen=3
hiInduct=0
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;UPDATE CHARTS
Function UpdateCharts(refresh)
 ;hardware sales
 hardOldLeader=hardLeader(0)
 hi=0 : hardLeader(0)=0
 For cyc=1 To no_machines
  If hardReleased(cyc)>0
   hardOldPromotion#(cyc)=hardPromotion#(cyc)
   If hardPromotion#(cyc)>0
    faller#=Rnd#(0.5,2.0);Float#(150-hardReplay(cyc))/50 
    If hardModel$(cyc)=hardCom$(cyc) Then faller#=faller#/2
    If optTime>1 Then faller#=faller#*2
    hardPromotion#(cyc)=hardPromotion#(cyc)-faller#
    If hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0
    hardUsers(cyc)=hardUsers(cyc)+GetHardwareSales(cyc)
    hardPeakUsers(cyc)=hardUsers(cyc)
   Else
    faller#=Rnd#(0.5,2.0)
    If hardModel$(cyc)=hardCom$(cyc) Then faller#=faller#/2
    If optTime>1 Then faller#=faller#*2
    hardPromotion#(cyc)=hardPromotion#(cyc)-faller#
    If hardPromotion#(cyc)<-99 Then hardPromotion#(cyc)=-99
    hardUsers(cyc)=PercentOf#(hardPeakUsers(cyc),100-Abs(hardPromotion#(cyc)))
   EndIf
   If hardUsers(cyc)<0 Then hardUsers(cyc)=0
   If hardUsers(cyc)>hi Then hardLeader(0)=cyc : hi=hardUsers(cyc)
  EndIf
 Next
 ;software sales
 gamSales=0
 chartGame(1)=projIdentity(gamRelease)
 chartFormat(1)=projFormat(gamRelease)
 chartCompany(1)=charFed(gamChar)
 chartChar(1)=gamChar
 FindIndustryStandards()
 For cyc=1 To 20
  ;wear down existing entries
  chartCompany(cyc)=charFed(chartChar(cyc))
  If cyc=1 Then faller#=Float#(250-GetPercent#(projReplay(gamRelease),hardStandard(2)))/25
  If cyc>1 Then faller#=Float#(250-GetPercent#(softReplay(chartGame(cyc)),hardStandard(2)))/25 
  If chartCompany(cyc)=no_feds Then faller#=faller#*2
  ;If optTime>1 Then faller#=faller#*2
  chartPromotion#(cyc)=chartPromotion#(cyc)-Rnd#(faller#,faller#*2)
  If chartPromotion#(cyc)<0 Then chartPromotion#(cyc)=0
  ;replace redundant entries
  If cyc>1
   randy=Rnd(0,10)
   If (randy=<1 And chartPromotion#(cyc)<10) Or chartPromotion#(cyc)<1 Or chartGame(cyc)=0 Or chartGame(cyc)=gamRelease Or refresh=1
    NewChartEntry(cyc)
   EndIf
  EndIf
  ;calculate sales
  chartSales(cyc)=0
  If cyc=1
   If gamFashion=1 Then multiplier=6 Else multiplier=2
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(projGameplay(gamRelease),hardStandard(1))*charRep(projCredit(gamRelease,1)))*multiplier)
   If gamFashion=2 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(projReplay(gamRelease),hardStandard(2))*charRep(projCredit(gamRelease,2)))*multiplier)
   If gamFashion=3 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(proj2D(gamRelease),hardStandard(3))*charRep(projCredit(gamRelease,3)))*multiplier)
   If gamFashion=4 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(proj3D(gamRelease),hardStandard(4))*charRep(projCredit(gamRelease,4)))*multiplier)
   If gamFashion=5 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(projSound(gamRelease),hardStandard(5))*charRep(projCredit(gamRelease,5)))*multiplier)
   If gamFashion=6 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(projMusic(gamRelease),hardStandard(6))*charRep(projCredit(gamRelease,6)))*multiplier)
   If projPirated(gamRelease)>0 Then chartSales(cyc)=chartSales(cyc)/2 
  Else
   stat=(softGameplay(chartGame(cyc))+hardGameplay(chartFormat(cyc)))/2;Rnd(softGameplay(chartGame(cyc)),hardGameplay(chartFormat(cyc)))
   If stat>hardGameplay(chartFormat(cyc)) Then stat=hardGameplay(chartFormat(cyc)) 
   If gamFashion=1 Then multiplier=6 Else multiplier=2
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(1))*charRep(chartChar(cyc)))*multiplier)
   stat=(softReplay(chartGame(cyc))+hardReplay(chartFormat(cyc)))/2;Rnd(softReplay(chartGame(cyc)),hardReplay(chartFormat(cyc)))
   If stat>hardReplay(chartFormat(cyc)) Then stat=hardReplay(chartFormat(cyc)) 
   If gamFashion=2 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(2))*charRep(chartChar(cyc)))*multiplier)
   stat=(soft2D(chartGame(cyc))+hard2D(chartFormat(cyc)))/2;Rnd(soft2D(chartGame(cyc)),hard2D(chartFormat(cyc)))
   If stat>hard2D(chartFormat(cyc)) Then stat=hard2D(chartFormat(cyc)) 
   If gamFashion=3 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(3))*charRep(chartChar(cyc)))*multiplier)
   stat=(soft3D(chartGame(cyc))+hard3D(chartFormat(cyc)))/2;Rnd(soft3D(chartGame(cyc)),hard3D(chartFormat(cyc)))
   If stat>hard3D(chartFormat(cyc)) Then stat=hard3D(chartFormat(cyc)) 
   If gamFashion=4 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(4))*charRep(chartChar(cyc)))*multiplier)
   stat=(softSound(chartGame(cyc))+hardSound(chartFormat(cyc)))/2;Rnd(softSound(chartGame(cyc)),hardSound(chartFormat(cyc)))
   If stat>hardSound(chartFormat(cyc)) Then stat=hardSound(chartFormat(cyc)) 
   If gamFashion=5 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(5))*charRep(chartChar(cyc)))*multiplier)
   stat=(softMusic(chartGame(cyc))+hardMusic(chartFormat(cyc)))/2;Rnd(softMusic(chartGame(cyc)),hardMusic(chartFormat(cyc)))
   If stat>hardMusic(chartFormat(cyc)) Then stat=hardMusic(chartFormat(cyc)) 
   If gamFashion=6 Then multiplier=5 Else multiplier=1
   chartSales(cyc)=chartSales(cyc)+((GetPercent#(stat,hardStandard(6))*charRep(chartChar(cyc)))*multiplier)
  EndIf
  If GetMonth(gamDate)=12 Then chartSales(cyc)=chartSales(cyc)*2
  chartSales(cyc)=PercentOf#(chartSales(cyc),Rnd(90,110))
  chartSales(cyc)=PercentOf#(chartSales(cyc),chartPromotion#(cyc))
  If chartCompany(cyc)=no_feds Then chartSales(cyc)=chartSales(cyc)/3
  ;hardware limitations
  chartLimited(cyc)=0
  factor#=GetPercent#(hardUsers(chartFormat(cyc)),1000000)
  If factor#>100 Then factor#=100+((factor#-100)/5)
  If factor#<50 Then chartLimited(cyc)=1
  chartSales(cyc)=PercentOf#(chartSales(cyc),factor#)
  chartSales(cyc)=Int(chartSales(cyc))
 Next 
 gamSales=chartSales(1)
 projSales(gamRelease)=projSales(gamRelease)+gamSales
 ;store old positions
 For count=1 To 20
  chartOldList(count)=chartList(count)
  chartList(count)=0
 Next
 For count=1 To 20
  chartOldRank(count)=chartRank(count)
  chartRank(count)=0
 Next
 ;find new rankings
 For cyc=1 To 20
  hi=-1 : leader=0
  For count=1 To 20
   If chartSales(count)>hi And chartRank(count)=0 Then hi=chartSales(count) : leader=count 
  Next
  chartList(cyc)=leader : chartRank(leader)=cyc
 Next
 ;update office posters
 For v=1 To 6
  deskPoster(v)=chartGame(Rnd(2,20))
  For count=1 To 2
   deskGame(v,count)=chartGame(Rnd(2,20)) 
  Next
 Next
End Function

;CREATE NEW CHART ENTRY
Function NewChartEntry(cyc)
    ;character
	Repeat
     newbie=Rnd(1,no_chars)
    Until charTeam(newbie)<>gamChar
    chartChar(cyc)=newbie
    chartCompany(cyc)=charFed(chartChar(cyc))
    ;hardware
    its=0
    Repeat
     chartFormat(cyc)=Rnd(1,no_machines) : its=its+1 : satisfied=1
     If its<100 And hardUsers(chartFormat(cyc))<100000 Then satisfied=0
     If hardReleased(chartFormat(cyc))=0 Then satisfied=0
    Until satisfied=1
    ;identity
    its=0
    Repeat
     chartGame(cyc)=Rnd(1,no_games) : its=its+1 : satisfied=1
     If soft2D(chartGame(cyc))>hard2D(chartFormat(cyc)) Or soft3D(chartGame(cyc))>hard3D(chartFormat(cyc)) Then satisfied=0
     If chartFormat(cyc)>1
      If soft2D(chartGame(cyc))<hard2D(chartFormat(cyc))/2 Or soft3D(chartGame(cyc))<hard3D(chartFormat(cyc))/2 Then satisfied=0
     EndIf
     ;conflicts
     For v=1 To no_projects
      If chartGame(cyc)=projIdentity(v) Then satisfied=0
     Next
     For v=1 To 20
      If cyc<>v And chartGame(cyc)=chartGame(v) Then satisfied=0
     Next
    Until satisfied=1 Or its>1000
    ;promotion
    If refresh=1 Then chartPromotion#(cyc)=Rnd#(25,100) Else chartPromotion#(cyc)=Rnd#(75,125)
    chartRank(cyc)=0
End Function

;GET CHARACTER RANKINGS
Function GetRankings(category)
 ;reset records
 For char=1 To no_chars
  charRanked(char)=0
  fedRank(fed,char)=0
 Next
 ;honour roster slots
 If category=0
  If fed=0
   slotter=0
   For promotion=1 To no_feds
    For count=1 To fedSize(promotion) 
     char=fedRoster(promotion,count)
     If screenAgenda<>1 Or fedLocked(promotion)=0
      slotter=slotter+1
      fedRank(fed,slotter)=char : charRanked(char)=slotter
     EndIf
    Next
   Next
  Else
   For count=1 To fedSize(fed)
    char=fedRoster(fed,count)
    fedRank(fed,count)=char : charRanked(char)=count
   Next
  EndIf
 EndIf
 ;rank according to character stats
 If category>0
  range=no_chars
  If fed>0 Then range=fedSize(fed)
  For finder=1 To range
   leader=0
   hi=-1
   For char=1 To no_chars
    excluded=0
    If fed>0 And charFed(char)<>fed Then excluded=1
    If charFed(char)=0 Then excluded=1
    If excluded=0 And charRanked(char)=0
     If category=1 And charDesign(char)>hi Then hi=charDesign(char) : leader=char
     If category=2 And charProgram(char)>hi Then hi=charProgram(char) : leader=char
     If category=3 And char2D(char)>hi Then hi=char2D(char) : leader=char
     If category=4 And char3D(char)>hi Then hi=char3D(char) : leader=char
     If category=5 And charSound(char)>hi Then hi=charSound(char) : leader=char
     If category=6 And charMusic(char)>hi Then hi=charMusic(char) : leader=char
     If category=7 And charRep(char)>hi Then hi=charRep(char) : leader=char
     If category=8 And charAge(char)>hi Then hi=charAge(char) : leader=char  
     If category=9 And charContract(char)>hi Then hi=charContract(char) : leader=char
     If category=10 And charSalary(char)>hi Then hi=charSalary(char) : leader=char
    EndIf
   Next
   fedRank(fed,finder)=leader : charRanked(leader)=finder
  Next
 EndIf
End Function

;GET CAREER RANKINGS
Function CareerRankings(category)
 ;reset checkers
 For count=1 To 10
  hiRank(count)=0
 Next
 For count=1 To 10
  charRanked(count)=0
 Next
 ;find top 10
 For finder=1 To 10
  hi=-1 : leader=0
  For cyc=1 To 10
   If hiChar(cyc)>0 And charRanked(cyc)=0
    If category=1 And hiWealth(cyc)>hi Then hi=hiWealth(cyc) : leader=cyc
    If category=2 And hiRep(cyc)>hi Then hi=hiRep(cyc) : leader=cyc
    If category=3 And hiExperience(cyc)>hi Then hi=hiExperience(cyc) : leader=cyc
   EndIf
  Next
  hiRank(finder)=leader : charRanked(leader)=finder
 Next
End Function

;RANK LABELS
Function RankLabels()
 ;reset and clock previous
 For label=1 To no_feds
  fedOldList(label)=fedList(label)
  fedList(label)=0
  fedOldRanked(label)=fedRanked(label)
  fedRanked(label)=0
 Next
 ;update list
 For finder=1 To no_feds
  hi=0 : leader=0
  For label=1 To 4
   If fedPopularity(label)>hi And fedRanked(label)=0 Then leader=label : hi=fedPopularity(label)
  Next
  fedList(finder)=leader : fedRanked(leader)=finder
 Next
End Function

;FIND LEADING CHARACTERS
Function FindLeadingCharacters()
 For category=1 To 8
  leader=0 : hi=-1
  For char=1 To no_chars
   If category=1 And charDesign(char)>hi Then hi=charDesign(char) : leader=char
   If category=2 And charProgram(char)>hi Then hi=charProgram(char) : leader=char
   If category=3 And char2D(char)>hi Then hi=char2D(char) : leader=char
   If category=4 And char3D(char)>hi Then hi=char3D(char) : leader=char
   If category=5 And charSound(char)>hi Then hi=charSound(char) : leader=char
   If category=6 And charMusic(char)>hi Then hi=charMusic(char) : leader=char
   If category=7 And charRep(char)>hi Then hi=charRep(char) : leader=char
   If category=8 And charSalary(char)>hi Then hi=charSalary(char) : leader=char
  Next
  charOldLeader(category)=charLeader(category) 
  charLeader(category)=leader
 Next
End Function