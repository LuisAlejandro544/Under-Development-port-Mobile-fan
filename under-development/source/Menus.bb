;//////////////////////////////////////////////////////////////////////////////
;------------------------ UNDER DEVELOPMENT: MENU SCREENS ---------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------- 2. OPTIONS -----------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Options()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
oldRes=optMenuRes
foc=0 : oldfoc=foc
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If MouseDown(1) And foc=10 Then go=1
	EndIf
	;manage music
	ManageMusic(0) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;search left
	 If MouseDown(2)
	  If foc=1 Then optStartDate=optStartDate-4 : PlaySound sMenuBrowse : keytim=1
	  If foc=2 Then optTime=optTime-1 : PlaySound sMenuBrowse : keytim=10
	  If foc=3 Then optJumble=optJumble-1 : PlaySound sMenuBrowse : keytim=5 
	  If foc=4 Then optCutscenes=optCutscenes-1 : PlaySound sMenuBrowse : keytim=5 
	  If foc=5 Then optEntourage=optEntourage-1 : PlaySound sMenuBrowse : keytim=5 
	  If foc=6 Then optMusicLoad=optMusicLoad-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=7 Then optMusicVolume=optMusicVolume-5 : PlaySound sMenuBrowse : keytim=5
	  If foc=8 Then optMenuRes=optMenuRes-1 : PlaySound sMenuBrowse : keytim=5
	 EndIf
	 ;search right
	 If MouseDown(1)
	  If foc=1 Then optStartDate=optStartDate+4 : PlaySound sMenuBrowse : keytim=1
	  If foc=2 Then optTime=optTime+1 : PlaySound sMenuBrowse : keytim=10
	  If foc=3 Then optJumble=optJumble+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=4 Then optCutscenes=optCutscenes+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=5 Then optEntourage=optEntourage+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=6 Then optMusicLoad=optMusicLoad+1 : PlaySound sMenuBrowse : keytim=5 
	  If foc=7 Then optMusicVolume=optMusicVolume+5 : PlaySound sMenuBrowse : keytim=5 
	  If foc=8 Then optMenuRes=optMenuRes+1 : PlaySound sMenuBrowse : keytim=5
	 EndIf 
	 ;revert
	 If KeyDown(14)
	  If foc=1 Then optStartDate=GetDate(1,1,1981) : PlaySound sTrash : keytim=20
	  If foc=8 Then optMenuRes=2 : PlaySound sTrash : keytim=20 
	 EndIf
	EndIf  
	;limits
	If optStartDate<GetDate(1,1,1981) Then optStartDate=GetDate(1,1,1981)
	If optStartDate>GetDate(1,12,2013) Then optStartDate=GetDate(1,12,2013) 
	If optTime<1 Then optTime=2
	If optTime>2 Then optTime=1 
	If optJumble<0 Then optJumble=0
	If optJumble>2 Then optJumble=2   
	If optCutscenes<0 Then optCutscenes=0
	If optCutscenes>2 Then optCutscenes=2  
	If optEntourage<0 Then optEntourage=0
	If optEntourage>2 Then optEntourage=2 
	If optMusicLoad<-1 Then optMusicLoad=-1
	If optMusicLoad>1 Then optMusicLoad=1 
	If optMusicVolume<0 Then optMusicVolume=0
	If optMusicVolume>100 Then optMusicVolume=100  
	If optMenuRes<2 Then optMenuRes=2
	If optMenuRes>no_resolutions Then optMenuRes=no_resolutions   
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(2),rX#(400),rY#(75) 
 ;options
 foc=0
 x=400 : y=170
 DrawOption(1,rX#(x),rY#(y),"Start Date",textMonth$(GetMonth(optStartDate))+" "+GetYear(optStartDate)) : y=y+40
 If optTime=2 Then namer$="Months" Else namer$="Weeks"
 DrawOption(2,rX#(x),rY#(y),"Time Frame",namer$) : y=y+45
 DrawOption(3,rX#(x),rY#(y),"Characters",textJumble$(optJumble)) : y=y+40
 DrawOption(4,rX#(x),rY#(y),"Cutscenes",textCutscenes$(optCutscenes)) : y=y+40 
 DrawOption(5,rX#(x),rY#(y),"Background Chars",textEntourage$(optEntourage)) : y=y+45
 namer$="Full Selection"
 If optMusicLoad=0 Then namer$="Reduced Selection"
 If optMusicLoad=-1 Then namer$="Greatly Reduced" 
 DrawOption(6,rX#(x),rY#(y),"Music Files",namer$) : y=y+40
 DrawOption(7,rX#(x),rY#(y),"Music Volume",optMusicVolume+"%") : y=y+45
 DrawOption(8,rX#(x),rY#(y),"Resolution",textResX$(optMenuRes)+" x "+textResY$(optMenuRes)) : y=y+45
 If screenAgenda=5 Then namer$=">>> PROCEED >>>" Else namer$="<<< BACK <<<"
 DrawOption(10,rX#(x),rY#(560),namer$,"")  
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf  
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack 
;change resolution
If optMenuRes<>oldRes
 ChangeResolution(optMenuRes,1)
EndIf
SaveOptions()
;begin career
If go=1 And screenAgenda=5
 Loader("Please Wait","Generating Career")
 SwitchSlot(0)
 slot=targetSlot
 gamInherit=0
 GenerateCareer()
 slotActive(slot)=1
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 SavePhotos(slot)  
 editChar=gamChar
 screen=51 : screenAgenda=5
Else
 screen=1
EndIf
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 16. SELECT GAME -------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectGame()
;category
gamDevelop=Rnd(1,12)
If gamDevelop>6 Then gamDevelop=7
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=0 : oldfoc=foc
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Or (MouseDown(1) And foc=22) Then go=-1
	 ;proceed
	 If MouseDown(1) And foc=>1 And foc=<20 Then go=1
	EndIf
	;manage music
	ManageMusic(0) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;change difficulty
	 If MouseDown(2) And foc=21 Then optDifficulty=optDifficulty-1 : PlaySound sMenuBrowse : keytim=1
	 If MouseDown(1) And foc=21 Then optDifficulty=optDifficulty+1 : PlaySound sMenuBrowse : keytim=1   
	 If optDifficulty<0 Then optDifficulty=0
	 If optDifficulty>100 Then optDifficulty=100   
	EndIf      
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(2),rX#(400),rY#(75) 
 ;options
 foc=0
 x=290 : y=200
 For cyc=1 To no_subgames
  DrawOption(cyc,rX#(x),rY#(y),textGame$(cyc),"")
  x=x+220
  If x>510 Then x=290 : y=y+50
 Next
 DrawOption(21,rX#(400),rY#(500),"Difficulty",optDifficulty+"%")
 DrawOption(22,rX#(400),rY#(560),"<<< BACK <<<","")  
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect  
 EndIf
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=1 Then PlaySound sMenuGo Else PlaySound sMenuBack 
If go=1 Then screen=60+foc Else screen=1
End Function

;////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS -----------------------
;////////////////////////////////////////////////////////////////

;DRAW MENU ITEM
Function DrawOption(box,x,y,scriptA$,scriptB$)
 ;mouse hotspots
 If scriptB$="" Then width=100 Else width=155
 If box=>0 And (editExamine=0 Or screen<>51) And (screenConfirm=0 Or box>1000)
  If MouseX()=>(x-width)+1 And MouseX()=<(x+width)+1 And MouseY()=>y-15 And MouseY()=<y+17 Then foc=box
 EndIf
 ;assess highlight
 highlight=0
 If foc=box Or box=-1 Then highlight=1
 ;draw solo box
 If scriptB$=""
  If highlight=1 Then DrawImage gMenu(1),x,y 
  If highlight=0 Then DrawImage gMenu(2),x,y
  SqueezeFont(scriptA$,175,20)
  If highlight=1 Then Outline(scriptA$,x,y+2,255,240,0,255,240,0)
  If highlight=0 Then Outline(scriptA$,x,y+2,175,175,175,175,175,175) 
 EndIf
 ;draw twin box
 If scriptB$<>""
  If highlight=1 Then DrawImage gMenu(5),x,y
  If highlight=0 Then DrawImage gMenu(6),x,y
  SqueezeFont(scriptA$,140,20)
  If highlight=1 Then Outline(scriptA$,x-78,y+2,255,240,0,255,240,0)
  If highlight=0 Then Outline(scriptA$,x-78,y+2,175,175,175,175,175,175)
  SqueezeFont(scriptB$,140,20)
  If highlight=1 Then Outline(scriptB$,x+78,y+2,255,255,255,255,255,255)
  If highlight=0 Then Outline(scriptB$,x+78,y+2,175,175,175,175,175,175)
 EndIf
 ;advice related to box
 If foc=box Then FindAdvice(x,y)
End Function

;SQUEEZE MAIN FONT INTO BOX
Function SqueezeFont(script$,width,height)
 f=30
 SetFont font(f)
 While f>0 And (StringWidth(script$)>width Or StringHeight(script$)>height)
  f=f-1
  SetFont font(f)
 Wend
End Function

;SQUEEZE NEWS FONT INTO BOX
Function SqueezeNewsFont(script$,width,height)
 If screen=23 Or screen=25 Then f=20 Else f=11
 SetFont fontNews(f)
 While f>0 And (StringWidth(script$)>width Or StringHeight(script$)>height)
  f=f-1
  SetFont fontNews(f)
 Wend
End Function

;BLOCK ACCESS
Function BlockAccess()
 go=0
 If keytim=0 Then PlaySound sVoid : keytim=10
End Function

;DRAW STAT BOX
Function DrawStatBox(rank,char,category,x,y)
 ;box base
 ;Color 140,90,170
 ;If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>category) Or subFoc=>20 Then Color 85,70,110
 ;Rect x-12,y-12,25,25,1
 ;box border
 Color 255,240,0
 If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>category) Or subFoc=>20 Then Color 175,175,175
 Rect x-12,y-14,25,29,0
 ;get figure
 stat=0
 If category=1 Then stat=charDesign(char) : GetStatColour(charDesign(char),charOldDesign(char),255,255,255)
 If category=2 Then stat=charProgram(char) : GetStatColour(charProgram(char),charOldProgram(char),255,255,255)
 If category=3 Then stat=char2D(char) : GetStatColour(char2D(char),charOld2D(char),255,255,255)
 If category=4 Then stat=char3D(char) : GetStatColour(char3D(char),charOld3D(char),255,255,255)
 If category=5 Then stat=charSound(char) : GetStatColour(charSound(char),charOldSound(char),255,255,255)
 If category=6 Then stat=charMusic(char) : GetStatColour(charMusic(char),charOldMusic(char),255,255,255)
 If category=7 Then stat=charRep(char) : GetStatColour(charRep(char),charOldRep(char),255,255,255)
 ;display text
 r=ColorRed() : g=ColorGreen() : b=ColorBlue()
 If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>category) Or subFoc=>20 Then r=r-(r/3) : g=g-(g/3) : b=b-(b/3)
 SetFont font(3)
 Outline(stat,x,y+1,0,0,0,r,g,b)
End Function

;DRAW SIDE BOX (in list)
Function DrawSideBox(cyc,namer$,x,y)
 SqueezeFont(namer$,90,15)
 If foc=cyc
  DrawImage gMenu(3),x,y 
  If screen=14 
   Outline(namer$,x,y+1,ColorRed(),ColorGreen(),ColorBlue(),ColorRed(),ColorGreen(),ColorBlue())
  Else
   Outline(namer$,x,y+1,255,255,255,255,255,255)
  EndIf
 Else
  DrawImage gMenu(4),x,y 
  Outline(namer$,x,y+1,ColorRed(),ColorGreen(),ColorBlue(),ColorRed(),ColorGreen(),ColorBlue())
 EndIf
End Function

;DRAW LABEL PROFILE
Function DrawLabelProfile(label)
 ;get co-ords
 x=rX#(400)-225 : y=rY#(26)
 ;ascertain colours
 If screen=>50 
  outR=0 : outG=0 : outB=0
  r1=70 : g1=70 : b1=250
  r2=255 : g2=255 : b2=255
 Else
  outR=240 : outG=240 : outB=240
  r1=0 : g1=0 : b1=200
  r2=0 : g2=0 : b2=0
 EndIf
 ;<<<<<<<<< LEFT HAND SIDE <<<<<<<<<
 ;photo & name
 Color 0,75,225 : Line x-56,y+8,x+66,y+8
 Color 0,0,150 : Line x-60,y+10,x+70,y+10
 If fedBooker(label)>0 Then DrawImage charPhoto(fedBooker(label)),x-90,y+36
 SqueezeFont(fedName$(label),150,18)
 Outline(fedName$(label),x+5,y-1,outR,outG,outB,r1,g1,b1)
 ;headers
 SetFont font(2)
 OutlineStraight("Popularity:",x-61,y+20,outR,outG,outB,r1,g1,b1)
 OutlineStraight("Reputation:",x-67,y+35,outR,outG,outB,r1,g1,b1)
 OutlineStraight("Bank:",x-34,y+50,outR,outG,outB,r1,g1,b1)
 OutlineStraight("Roster:",x-47,y+65,outR,outG,outB,r1,g1,b1)
 OutlineStraight("Owner:",x-47,y+80,outR,outG,outB,r1,g1,b1)
 ;statistics
 SetFont font(3)
 GetStatColour(fedPopularity(label),fedOldPopularity(label),r2,g2,b2)
 OutlineStraight(fedPopularity(label)+"%",x+9,y+20,outR,outG,outB,ColorRed(),ColorGreen(),ColorBlue())
 GetStatColour(fedReputation(label),fedOldReputation(label),r2,g2,b2)
 OutlineStraight(fedReputation(label)+"%",x+7,y+35,outR,outG,outB,ColorRed(),ColorGreen(),ColorBlue())
 OutlineStraight("$"+GetFigure$(fedBank(label)),x+5,y+50,outR,outG,outB,r2,g2,b2)
 OutlineStraight(fedSize(label)+" Artists",x+3,y+65,outR,outG,outB,r2,g2,b2)
 If fedBooker(label)>0 Then namer$=charName$(fedBooker(label)) Else namer$="Vacant"
 OutlineStraight(namer$,x+1,y+80,outR,outG,outB,r2,g2,b2)
End Function

;DRAW CHARACTER PROFILE
Function DrawCharProfile(char,x,y,detail) ;1=full, 0=stats alone
 ;offset
 x=x-65
If detail=1
 ;photo
 If screen<>3 Or char=5 Then DrawImage gLabel(charFed(char)),x-185,y+75
 If char>0 Then DrawImage charPhoto(char),x-102,y+85
 ;name
 SqueezeFont(charName$(char),200,20)
 OutlineStraight(charName$(char),x,y+11,0,0,0,255,255,255)
 spacer=StringWidth(charName$(char))
 SetFont font(3)
 OutlineStraight("("+charAge(char)+"yrs)",(x+spacer)+6,y+12,0,0,0,200,200,200)
 ;descriptions
 SetFont font(3)
 Outline("Design",x-50,y+33,0,0,0,255,255,255)
 Outline("Program",x-55,y+54,0,0,0,255,255,255)
 Outline("2D Art",x-50,y+75,0,0,0,255,255,255)
 Outline("3D Art",x-50,y+96,0,0,0,255,255,255)
 Outline("Sound",x-47,y+117,0,0,0,255,255,255)
 Outline("Music",x-47,y+138,0,0,0,255,255,255)
 ;Outline("Rep",x+248,y+152,0,0,0,255,255,255)
 ;contract
 If screen<>3 Then DrawContract(char,x+330,y+80)
EndIf
 ;meters
 DrawMeter(1,x-12,y+32,charDesign(char),charOldDesign(char),99)
 DrawMeter(2,x-12,y+53,charProgram(char),charOldProgram(char),99)
 DrawMeter(3,x-12,y+74,char2D(char),charOld2D(char),99)
 DrawMeter(4,x-12,y+95,char3D(char),charOld3D(char),99)
 DrawMeter(5,x-12,y+116,charSound(char),charOldSound(char),99)
 DrawMeter(6,x-12,y+137,charMusic(char),charOldMusic(char),99)
 DrawVertMeter(7,x+248,y+138,charRep(char),charOldRep(char),99)
End Function

;DRAW CONTRACT
Function DrawContract(char,x,y)
 DrawImage gContract,x,y
 SetFont fontNews(1)
 If charContract(char)>0 Then namer$=charContract(char)+" "+textPeriod$(optTime)+"s" Else namer$="Freelance"
 Outline(namer$,x-13,y-19,0,0,0,0,0,0)
 SetFont fontNews(2)
 If char=gamChar Then namer$=charRoyalties(char)+"%" Else namer$="$"+GetFigure$(charSalary(char))
 Outline(namer$,x-13,y,0,0,0,0,0,0)
 ;happiness
 If char<>gamChar
  If charHappiness(char)<-1 Then DrawImage gSmiley(1),x+28,y-43
  If charHappiness(char)=>-1 And charHappiness(char)=<1 Then DrawImage gSmiley(2),x+28,y-43
  If charHappiness(char)>1 Then DrawImage gSmiley(3),x+28,y-43
 EndIf
End Function

;DRAW HARDWARE PROFILE
Function DrawHardwareProfile(cyc,x,y,detail) ;1=full, 0=stats alone
 ;offset
 x=x-50
If detail=1
 ;photo
 DrawImage gHardware(cyc),rX#(400)-180,y+75
 ;price tag
 If hardOwned(cyc)=0
  DrawImage gMoney,rX#(400)-260,y+125
  SetFont fontNews(5)
  OutlineStraight("$"+GetFigure$(hardCost(cyc)),rX#(400)-225,y+140,0,0,0,100,200,100)
 EndIf
 ;name
 SqueezeFont(hardTitle$(cyc),200,20)
 OutlineStraight(hardTitle$(cyc),x-15,y+11,0,0,0,255,255,255)
 spacer=StringWidth(hardTitle$(cyc))
 SetFont font(3)
 OutlineStraight("("+Left$(textMonth$(hardMonth(cyc)),3)+" "+hardYear(cyc)+")",((x-15)+spacer)+6,y+12,0,0,0,200,200,200)
 ;format
 DrawImage gFormat(hardFormat(cyc)),x+285,y+75 
 If hardPirated(cyc)>0 Then DrawImage gPiracy,x+285,y+75 
 namer$="$"+hardRetail(cyc)+" -$"+formatCost(hardFormat(cyc))
 SetFont fontNews(3) : Outline(namer$,x+285,y+31,0,0,0,150,200,150)
 SetFont font(3) : Outline(GetFigure$(hardUsers(cyc)),x+285,y+120,0,0,0,200,200,250)
 SetFont font(1) : Outline("active users",x+285,y+136,0,0,0,200,200,250)
 ;Outline(formatName$(hardFormat(cyc)),x+300,y+115,0,0,0,255,255,255)
EndIf
 ;meters
 DrawMeter(1,x-12,y+32,hardGameplay(cyc),hardOldGameplay(cyc),99)
 DrawMeter(2,x-12,y+53,hardReplay(cyc),hardOldReplay(cyc),99)
 DrawMeter(3,x-12,y+74,hard2D(cyc),hardOld2D(cyc),99)
 DrawMeter(4,x-12,y+95,hard3D(cyc),hardOld3D(cyc),99)
 DrawMeter(5,x-12,y+116,hardSound(cyc),hardOldSound(cyc),99)
 DrawMeter(6,x-12,y+137,hardMusic(cyc),hardOldMusic(cyc),99)
End Function

;DRAW HORIZONTAL STAT METER
Function DrawMeter(cyc,x,y,current,old,max)
 If cyc=>1 And cyc=<7 Then DrawImage gIconSmall(cyc),x,y
 If max<99
  DrawImageRect gMeter(0),x+113,y,0,0,4+(max*2),22
 Else
  DrawImage gMeter(0),x+113,y
 EndIf
 If current>max Then current=max
 If game=0 Or current=old Then DrawImageRect gMeter(1),x+113,y,0,0,4+(current*2),22
 If game=1 And current>old Then DrawImageRect gMeter(2),x+113,y,0,0,4+(current*2),22
 If game=1 And current<old Then DrawImageRect gMeter(3),x+113,y,0,0,4+(current*2),22
 SetFont font(3)
 GetStatColour(current,old,255,255,255)
 OutlineStraight(current+"%",(x+19)+(max*2),y,0,0,0,ColorRed(),ColorGreen(),ColorBlue())
End Function

;DRAW VERTICAL STAT METER
Function DrawVertMeter(cyc,x,y,current,old,max)
 If cyc=>1 And cyc=<7 Then DrawImage gIconSmall(cyc),x,y-1
 If max<99
  DrawImageRect gVertMeter(0),x,(y-61)+(102-max),0,106-(max+4),21,106-(106-(max+4))
 Else
  DrawImage gVertMeter(0),x,y-61
 EndIf
 If current>max Then current=max
 shower=1
 If game=1 And current>old Then shower=2
 If game=1 And current<old Then shower=3
 DrawImageRect gVertMeter(shower),x,(y-61)+(102-current),0,106-(current+4),21,106-(106-(current+4))
 SetFont font(3)
 GetStatColour(current,old,255,255,255)
 Outline(current+"%",x+2,(y-22)-max,0,0,0,ColorRed(),ColorGreen(),ColorBlue())
End Function

;DISPLAY TEAM MEMBERS
Function DisplayTeam(team,x,y)
 spot=1
 For count=1 To 6
  member=charRoster(team,count)
  If member>0 And member<>team And (charVacant(member)=0 Or game=0)
   spot=spot+1
   If spot=6 Then DrawImage charPhoto(member),x-30,y-2
   If spot=5 Then DrawImage charPhoto(member),x+20,y-1
   If spot=4 Then DrawImage charPhoto(member),x-20,y-1
   If spot=3 Then DrawImage charPhoto(member),x-10,y-2
   If spot=2 Then DrawImage charPhoto(member),x+10,y-2  
  EndIf
 Next
 DrawImage charPhoto(team),x,y
End Function

;GET STAT COLOUR
Function GetStatColour(current,old,r,g,b)
 Color r,g,b
 If game=1
  If b=>100
   If current>old Then Color 100,220,100
   If current<old Then Color 220,100,100
  Else
   If current>old Then Color 0,175,0
   If current<old Then Color 175,0,0
  EndIf
 EndIf
End Function

;LOADING DISPLAY
Function Loader(scriptA$,scriptB$)
 If scriptA$<>"" Then loadMessageA$=scriptA$
 If scriptB$<>"" Then loadMessageB$=scriptB$
 If screen>0 
  Cls
  ;border
  Color Rnd(0,10)*25,Rnd(0,10)*25,Rnd(0,10)*25
  Rect rX#(0),rY#(0),rX#(800),rY#(80)
  Color Rnd(0,10)*25,Rnd(0,10)*25,Rnd(0,10)*25
  Rect rX#(0),rY#(520),rX#(800),rY#(80)
  For count=1 To 15
   Color Rnd(0,10)*25,Rnd(0,10)*25,Rnd(0,10)*25
   Rect rX#(0),rY#(Rnd(0,600)),rX#(800),rY#(Rnd(10,100))
  Next 
  Color 0,0,0
  Rect rX#(80),rY#(80),rX#(640),rY#(440)
  ;message
  SetFont font(20)
  Color 255,255,255
  Text rX#(400),rY#(300)-20,loadMessageA$,1,1
  Text rX#(400),rY#(300)+20,loadMessageB$,1,1
  Flip
 Else
  ;intro loader
  DrawOption(-1,rX#(400),rY#(565),loadMessageA$,loadMessageB$)
  Flip
 EndIf
End Function

;QUICK LOADING DISPLAY
Function QuickLoader(x#,y#,scriptA$,scriptB$)
 DrawOption(-1,x#,y#,scriptA$,scriptB$)
 Flip
End Function

;CHANGE RESOLUTION
Function ChangeResolution(resolution,task) ;0=pre-game, 1=during game
 ;assess preferences
 width=Int(textResX$(resolution))
 height=Int(textResY$(resolution))
 If GfxMode3DExists(width,height,16)=0 Then width=800 : height=600 : optMenuRes=2
 ;make transition?
 If width<>GraphicsWidth() Or height<>GraphicsHeight()
  If task>0 Then Loader("Please Wait","Adjusting Resolution")
  Graphics3D width,height,16,1+optWindow
  If task>0 ;restore media
   LoadFonts()
   Loader("","") : LoadImages()
   Loader("Please Wait","Restoring Media")
   LoadTextures()
   Loader("","") : LoadPhotos(slot)
   Loader("","") : LoadProjectImages()
   LoadGameBox(0) : LoadGameScreen(0)
   LoadGameBox(418) : LoadGameScreen(418)
  EndIf
 EndIf
End Function

;GET SCREENSHOT
Function Screenshot()   
 ;obtain image
 PlaySound sCamera
 screenshot=CreateImage(GraphicsWidth(),GraphicsHeight())
 GrabImage screenshot,GraphicsWidth()/2,GraphicsHeight()/2
 ;title & save
 temp=MilliSecs()/10
 namer$="Screenshot - "+temp+".bmp"
 If game=1 And slot>0
  namer$=charName$(gamChar)+" ("+Left$(textMonth$(GetMonth(gamDate)),3)+" "+GetYear(gamDate)+") - "+temp+".bmp"
 EndIf
 SaveImage(screenshot,"Photo Album/"+namer$)
 FreeImage screenshot
End Function

;MANAGE MUSIC
Function ManageMusic(track)
 ;request switch
 randy=Rnd(0,10000)
 If randy=<3 And chThemeChange=0
  Repeat
   newbie=Rnd(1,no_themes)
  Until newbie<>chCurrentTheme
  chThemeChange=newbie
 EndIf
 If track>0 Then chThemeChange=track
 ;pursue target volume
 chThemeTarget#=PercentOf#(1.0,optMusicVolume) : speeder#=0.01
 If screen=50 Or (screen=>52 And screen=<54) Or screen=>61 Then chThemeTarget#=chThemeTarget#/2 : speeder#=0.01
 If chThemeChange>0 Then chThemeTarget#=0
 If chCurrentTheme=0 Then speeder#=0.005
 If chThemeVol#<chThemeTarget# Then chThemeVol#=chThemeVol#+speeder#
 If chThemeVol#>chThemeTarget# Then chThemeVol#=chThemeVol#-speeder#
 If chThemeVol#<0 Then chThemeVol#=0
 If chThemeVol#>1.0 Then chThemeVol#=1.0
 ;make switch
 If chThemeVol#=<0.01 And chThemeChange>0
  StopChannel(chTheme)
  LoopSound sTheme(chThemeChange)
  chTheme=PlaySound(sTheme(chThemeChange))
  chCurrentTheme=chThemeChange : chThemeChange=0
 EndIf 
 ChannelVolume chTheme,chThemeVol#
End Function