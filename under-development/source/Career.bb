;//////////////////////////////////////////////////////////////////////////////
;------------------------ UNDER DEVELOPMENT: CAREER ISSUES --------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;-------------------------- 27. CONFIRM UNIVERSE ------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function ConfirmUniverse()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc
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
	 ;cancel
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If MouseDown(1)
	  If foc=3 Then go=-1 Else go=1
	 EndIf
	EndIf 
	;manage music
	ManageMusic(0)  
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gLogo(2),rX#(400),rY#(60)
 ;DrawImage gMDickie,rX#(400),rY#(530) 
 ;message
 SetFont font(10)
 Outline("This folder already has a universe on file.",rX#(400),rY#(210),0,0,0,255,255,255)
 Outline("Would you like to insert a new character",rX#(400),rY#(250),0,0,0,255,255,255)
 Outline("into that one or start from scratch?",rX#(400),rY#(290),0,0,0,255,255,255) 
 ;options
 DrawOption(1,rX#(400),rY#(385),"INHERIT UNIVERSE","")  
 DrawOption(2,rX#(400),rY#(435),"REPLACE UNIVERSE","") 
 DrawOption(3,rX#(400),rY#(560),"<<< CANCEL <<<","")   
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect  
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed
If go>0 Then PlaySound sMenuGo Else PlaySound sMenuBack
FreeTimer timer
;re-use slot
If go=1 And foc=1
 Loader("Please Wait","Generating Career")
 SwitchSlot(targetSlot)
 gamInherit=1
 GenerateCareer() 
 slotActive(slot)=1
 SaveUniverse()
 SaveProgress(slot)
 SaveWorld(slot)
 SaveChars(slot)
 SavePhotos(slot)  
 editChar=gamChar
 screen=51 : screenAgenda=5
EndIf
;reset slot
If go=1 And foc=2
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
EndIf
;cancel
If go=-1 Then slotActive(slot)=0 : screen=10
End Function

;//////////////////////////////////////////////////////////////////////////////
;----------------------------- 20. CAREER HUB ---------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function CareerHub()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
screenConfirm=0
foc=0 : oldfoc=0 : subfoc=0
lastfoc=1
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>40 Then flashTim=0   
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then go=-1
	 ;retire
	 If KeyDown(19) And optCutscenes=<1 Then PlaySound sMenuGo : screenConfirm=5 : keytim=20 
	 If KeyDown(19) And optCutscenes=2 Then go=2
	 ;proceed
	 If MouseDown(1)
	  If foc=>1 And foc=<7 Then go=1 : screenConfirm=0
	  If foc=8 Then go=-1
	  ;release issues
	  If optCutscenes=<1
	   If foc=3 And projIdentity(gamProject)=<0
	    PlaySound sVoid :  : keytim=20
	    If projIdentified(gamProject)>0 Then screenConfirm=4 Else screenConfirm=2
	    go=0
	   EndIf
	   If foc=3 And projIdentity(gamProject)>0 And projIdentified(gamProject)<0 Then go=0 : PlaySound sVoid : screenConfirm=3 : keytim=20
	  EndIf
	  If foc=3 And projIdentified(gamProject)>0 And projIdentity(gamProject)=projIdentified(gamProject) And projReleased(gamProject)=0
	   If (fedPolicy(charFed(gamChar))=6 And AverageProjectStats(gamProject,1)<AverageHardwareStats(gamHardware)-5) Or (gamMission=>1 And gamMission=<6)
	    If optCutscenes=2
	     go=1
	    Else
	     go=0 : PlaySound sVoid : screenConfirm=6 : keytim=20
	    EndIf
	   Else
	    go=0 : PlaySound sMenuGo : screenConfirm=1 : keytim=20
	   EndIf
	  EndIf
	 EndIf
	 ;confirmations
	 If MouseDown(1) And foc=1001
	  If screenConfirm=>1 And screenConfirm=<4 Then foc=3 : screenConfirm=0 : go=1 : keytim=20
	  If screenConfirm=5 Then screenConfirm=0 : go=2 : keytim=20
	 EndIf
	 ;confirm cancel
	 If MouseDown(1) And foc=1002
	  PlaySound sMenuBack : keytim=20
	  screenConfirm=0
	 EndIf 
	 ;date cheat
	 If MouseX()<210 And MouseY()<180 ;KeyDown(46)
	  If KeyDown(12) Then gamDate=gamDate-4 : PlaySound sMenuBrowse : keytim=2
	  If KeyDown(13) Then gamDate=gamDate+4 : PlaySound sMenuBrowse : keytim=2 
	  If keytim>0 And gamDate<GetDate(1,1,1981) Then gamDate=GetDate(1,1,1981)
	 EndIf
	 ;money cheat
	 If MouseX()>590 And MouseY()<180 ;KeyDown(48)
	  If KeyDown(12) Then gamBank=gamBank-10000 : PlaySound sCash : keytim=5
	  If KeyDown(13) Then gamBank=gamBank+10000 : PlaySound sCash : keytim=5 
	 EndIf
	EndIf
	;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;headers
If go=0
 DrawImage gLabel(charFed(gamChar)),rX#(400),rY#(80)
 DisplayCalendar(gamDate,rX#(110),rY#(80))
 DrawContract(gamChar,rX#(565),rY#(80))
 DisplayMoney(gamBank,rX#(690),rY#(80))
 ;roster
 x=rX#(275) : y=rY#(80)
 For cyc=1 To optTeamSize
  If charRoster(gamChar,cyc)>0 And charVacant(charRoster(gamChar,cyc))=0
   pX#(cyc)=x : pY#(cyc)=y
   x=x-18 : y=y-1
  EndIf
 Next
 For count=1 To optTeamSize
  cyc=(optTeamSize-count)+1
  If charRoster(gamChar,cyc)>0 And charVacant(charRoster(gamChar,cyc))=0
   DrawImage charPhoto(charRoster(gamChar,cyc)),pX#(cyc),pY#(cyc)
  EndIf
 Next 
 ;options
 foc=0
 y=385
 DrawOption(1,rX#(400)-110,rY#(y),"PROJECTS","") 
 If projReleased(gamProject)>0 Then namer$="VIEW" Else namer$="DEVELOP" 
 DrawOption(2,rX#(400)-110,rY#(y+42),namer$,"")
 namer$="IDENTIFY"
 If projIdentified(gamProject)>0 Then namer$="RELEASE" 
 If projReleased(gamProject)>0 Then namer$="REVIEW"  
 DrawOption(3,rX#(400)-110,rY#(y+84),namer$,"")
 DrawOption(4,rX#(400)+110,rY#(y),"HARDWARE","")
 DrawOption(5,rX#(400)+110,rY#(y+42),"OFFICE","")
 DrawOption(6,rX#(400)+110,rY#(y+84),"CHARACTER","")
 DrawOption(7,rX#(400),rY#(520),">>> PROCEED >>>","") 
 DrawOption(8,rX#(400),rY#(565),"<<< EXIT <<<","")  
 ;profiles
 shower=1
 If lastfoc=4 Then shower=2
 ;If lastfoc=5 Then shower=4
 If lastfoc=6 Then shower=3
 If shower=1 Then DrawProjectProfile(gamProject,rX#(400),rY#(190),1)
 If shower=2 Then DrawHardwareProfile(gamHardware,rX#(400),rY#(190),1)
 If shower=3 Then DrawCharProfile(gamChar,rX#(400),rY#(190),1)
 If shower=4
  x=110 : y=270
  For cyc=1 To 6 
   If gamDesk(cyc)>0 Then DrawImage gDesk(1),rX#(x),rY#(y) Else DrawImage gDesk(0),rX#(x),rY#(y)
   If charRoster(gamChar,cyc)>0
    If charVacant(charRoster(gamChar,cyc))>0
     DrawImage gAbsent,rX#(x)+30,rY#(y)-5
    Else 
     DrawImage charPhoto(charRoster(gamChar,cyc)),rX#(x)+30,rY#(y)-5
    EndIf
   EndIf
   If gamDesk(cyc)<0 Then DrawImage gPiracy,rX#(x),rY#(y)
   x=x+120
  Next
 EndIf
 ;confirmations
 If screenConfirm>0
  x=rX#(400) : y=rY#(300)
  DrawImage gPrompt,x,y
  SetFont font(12)
  If screenConfirm=1
   Outline("Are you sure you",x,y-60,0,0,0,255,255,255)
   Outline("want to release",x,y-25,0,0,0,255,255,255)
   Outline("'"+projTitle$(gamProject)+"'?",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2
   Outline("There is no content to",x,y-42,0,0,0,255,255,255)
   Outline("identify in this game yet!",x,y-8,0,0,0,255,255,255)
  EndIf
  If screenConfirm=3
   Outline("You have already attempted to",x,y-42,0,0,0,255,255,255)
   Outline("identify this project today!",x,y-8,0,0,0,255,255,255)
  EndIf
  If screenConfirm=4
   Outline("'"+projTitle$(gamProject)+"'",x,y-60,0,0,0,255,255,255)
   Outline("cannot be released until",x,y-25,0,0,0,255,255,255)
   Outline("it is complete!",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=5
   Outline("Are you sure you want",x,y-42,0,0,0,255,255,255)
   Outline("to retire this career?",x,y-8,0,0,0,255,255,255)
  EndIf
  If screenConfirm=6
   Outline("'"+projTitle$(gamProject)+"'",x,y-60,0,0,0,255,255,255)
   Outline("is not good enough for",x,y-25,0,0,0,255,255,255)
   Outline(fedName$(charFed(gamChar))+" to release!",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2 Or screenConfirm=3 Or screenConfirm=4 Or screenConfirm=6
   DrawOption(1002,x,y+100,"OK","") 
  Else
   DrawOption(1001,x-115,y+100,"CONFIRM","") 
   DrawOption(1002,x+115,y+100,"CANCEL","") 
  EndIf
 EndIf

EndIf
 ;cursor
 If foc=>1 And foc=<6 Then lastfoc=foc
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
editFoc=0 : editScroll=0 : editFilter=0
;access option
If go=1 
 If foc=1 Then screen=13 : screenAgenda=0 ;projects
 If foc=2
  If projReleased(gamProject)>0 Then screenAgenda=0 Else screenAgenda=2 ;develop
  screen=50
 EndIf
 If foc=3
  approved=1
  If fedPolicy(charFed(gamChar))=6 And AverageProjectStats(gamProject,1)<AverageHardwareStats(gamHardware)-5 Then approved=0
  If gamMission=>1 And gamMission=<6 Then approved=0
  If approved=1 And projIdentified(gamProject)>0 And projIdentity(gamProject)=projIdentified(gamProject)
   gamRelease=gamProject : screen=29 : screenAgenda=0 ;release
  Else
   screen=50 : screenAgenda=6 ;identify
  EndIf
 EndIf
 If foc=4 Then hardFoc=0 : hardScroll=0 : screen=14 : screenAgenda=0 ;hardware
 If foc=5 Then screen=15 : screenAgenda=0 ;office
 If foc=6 Then editChar=gamChar : screen=51 : screenAgenda=1 ;edit character
 ;proceed
 If foc=7
  screen=23
  If gamMission=14 And gamDate=>gamDeadline
   If projReleased(gamTarget)=0 Then gamRelease=gamTarget : gamMission=0 : screen=29 : screenAgenda=0 ;forced release
  EndIf
 EndIf
EndIf
;retire
If go=2 And optCutscenes=<1 Then screen=25 : screenAgenda=0
If go=2 And optCutscenes=2 Then negTopic=1 : negChar=fedBooker(charFed(gamChar)) : negSetting=1 : screen=53 : screenAgenda=0
;save & exit
If go=-1
 Loader("Please Wait","Saving Career")
 ;GrabImage slotPreview(slot),(rX#(400)-225)-10,rY#(26)+37
 ;SaveImage(slotPreview(slot),"Data/Previews/Preview0"+slot+".bmp")
 slotName$(slot)=charName$(gamChar)
 slotDate(slot)=gamDate
 SaveUniverse()
 Loader("","") : SaveProgress(slot)
 Loader("","") : SaveWorld(slot)
 Loader("","") : SaveChars(slot)
 screen=10
EndIf
End Function

;//////////////////////////////////////////////////////////////////////////////
;-------------------------------- 13. PROJECTS --------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectProject()
;new project slot
no_boxes=no_projects
If no_projects<99 Then no_boxes=no_projects+1
ResetProject(no_projects+1)
projTitle$(no_projects+1)="* NEW PROJECT *"
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
optGrid=0 : scroll=0
foc=no_boxes : oldfoc=foc
lastfoc=no_projects
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>20 Then flashTim=0   
	
	;PORTAL
	gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then PlaySound sMenuBack : go=-1 
	 If MouseDown(1) And foc=999 Then PlaySound sMenuBack : go=-1
	 ;activate
	 If MouseDown(1) And foc=>1 And foc=<no_boxes And subFoc=-1
	  If gamAgreement(1)>0 And foc<>gamProject Then PlaySound sVoid : screenConfirm=1 : keytim=20
	  If gamMission=>1 And gamMission=<6 And foc<>gamProject Then PlaySound sVoid : screenConfirm=2 : keytim=20
	  If screenConfirm=0
	   PlaySound sProduce
	   gamProject=foc : gamHardware=projFormat(foc)
	   If gamProject>no_projects
	    no_projects=no_projects+1
	    ResetProject(no_projects)
	    projTitle$(no_projects)="Project #"+Dig$(no_projects,10)
	   EndIf
	   go=1
	  EndIf
	 EndIf
	 ;switch display
	 If KeyDown(15) Or MouseDown(2)
	  PlaySound sMenuBrowse : keytim=10
	  If optGrid=1 Then optGrid=0 Else optGrid=1
	  scroll=0
	 EndIf
	 ;confirmations
	 If MouseDown(1) And foc=1001

	 EndIf
	 ;confirm cancel
	 If MouseDown(1) And foc=1002
	  PlaySound sMenuBack : keytim=20
	  screenConfirm=0
	 EndIf 
	EndIf
	;manage music
	ManageMusic(0)
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And foc=>1 And foc=<no_projects And subFoc=-1
	 ;reset
	 If KeyDown(14) Or KeyDown(211)
	  If gamAgreement(1)=0 Or foc<>gamProject
	   PlaySound sTrash : keytim=10
	   ResetProject(foc)
	  EndIf
	 EndIf
	 ;generate identity 
	 If KeyDown(56) And KeyDown(57)
	  PlaySound sMenuGo : keytim=5
	  projGameplay(foc)=Rnd(0,hardGameplay(gamHardware))
	  projReplay(foc)=Rnd(0,hardReplay(gamHardware))
	  proj2D(foc)=Rnd(0,hard2D(gamHardware))
	  proj3D(foc)=Rnd(0,hard3D(gamHardware))
	  projSound(foc)=Rnd(0,hardSound(gamHardware))
	  projMusic(foc)=Rnd(0,hardMusic(gamHardware))
	  projOldIdentity(foc)=0 : projIdentity(foc)=IdentifyProject(foc)
	  ;projIdentified(foc)=projIdentity(foc)
	  projTitle$(foc)=softTitle$(projIdentity(foc))
	  Loader("Please Wait","Updating Project")
      LoadProjectBox(foc)
      LoadProjectScreen(foc)
	 EndIf 
	EndIf
	
	;SCROLLING
	If optGrid=1
	 ;grid display
	 no_displays=(Int((rY#(525)-rY#(210))/38)+1)*3
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=5
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=5
	  EndIf
	  If scroll>0 Then scroll=0
	  excess=Int(((no_boxes-no_displays)-1)/3)+1
	  If scroll<-(excess*38) Then scroll=-(excess*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   excess=Int(((foc-no_displays)-1)/3)+1
	   If foc>no_displays And scroll>-(excess*38) Then scroll=-(excess*38)
	   recess=Int((foc-1)/3)
	   If scroll<-(recess*38) Then scroll=-(recess*38) 
	  EndIf
     EndIf 
	Else
	 ;list display
	 no_displays=Int((rY#(525)-rY#(210))/38)+1
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   scrollSpeed=4
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	  EndIf
	  If scroll>0 Then scroll=0
	  If scroll<-((no_boxes-no_displays)*38) Then scroll=-((no_boxes-no_displays)*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   If foc>no_displays And scroll>-((foc-no_displays)*38) Then scroll=-((foc-no_displays)*38)
	   If scroll<-((foc-1)*38) Then scroll=-((foc-1)*38) 
	  EndIf
     EndIf 
	EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;profile
 If lastfoc=>1 And lastfoc=<no_boxes
  DrawProjectProfile(lastfoc,rX#(400),rY#(25),1)
 EndIf
 ;scrollers
 subFoc=-1
 If optGrid=1 Then indent=345 Else indent=345
 If no_boxes>no_displays 
  If scroll<0
   y=rY#(210)+58
   If MouseY()>y-72 And MouseY()<y+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=20
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=20
   EndIf
   If subFoc=20
    DrawImage gScroll(3),rX#(400)-indent,y
    DrawImage gScroll(3),rX#(400)+indent,y
   Else
    DrawImage gScroll(1),rX#(400)-indent,y
    DrawImage gScroll(1),rX#(400)+indent,y
   EndIf
  EndIf
  excess=Int(((no_boxes-no_displays)-1)/3)+1
  If (optGrid=0 And scroll>-((no_boxes-no_displays)*38)) Or (optGrid=1 And scroll>-(excess*38))
   y=rY#(520)-58
   If MouseY()>y-72 And MouseY()<y+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=21
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=21
   EndIf
   If subFoc=21
    DrawImage gScroll(4),rX#(400)-indent,y
    DrawImage gScroll(4),rX#(400)+indent,y
   Else
    DrawImage gScroll(2),rX#(400)-indent,y
    DrawImage gScroll(2),rX#(400)+indent,y
   EndIf
  EndIf
 EndIf
 ;PROJECT BOXES
 foc=0
 If optGrid=0 Then x=rX#(400)-205 : listY=rY#(210)
 If optGrid=1 Then x=rX#(400)-205 : listY=rY#(210)
 For cyc=1 To no_boxes
  y=listY+scroll
  If y=>rY#(210) And y=<rY#(525)
   ;mouse hotspots
   If screenConfirm=0
    If optGrid=1
     If MouseX()=>x-101 And MouseX()=<x+101 And MouseY()=>y-15 And MouseY()=<y+18 Then foc=cyc
    Else
     If MouseX()=>x-101 And MouseX()=<x+519 And MouseY()=>y-15 And MouseY()=<y+18 Then foc=cyc
    EndIf
   EndIf
   ;name box
   SqueezeFont(projTitle$(cyc),175,20)
   If foc=cyc
    DrawImage gMenu(1),x,y 
    Outline(projTitle$(cyc),x,y+1,255,240,0,255,240,0)
   Else
    DrawImage gMenu(2),x,y 
    Outline(projTitle$(cyc),x,y+1,175,175,175,175,175,175)
   EndIf
   ;additional
   If optGrid=0 And cyc=<no_projects
    DrawSideBox(cyc,hardTitle$(projFormat(cyc)),x+154,y)
    namer$=Left$(textMonth$(GetMonth(projDate(cyc))),3)+" "+GetYear(projDate(cyc))
    If projReleased(cyc)>0 Then namer$=Left$(textMonth$(GetMonth(projReleased(cyc))),3)+" "+GetYear(projReleased(cyc)) 
    DrawSideBox(cyc,namer$,x+258,y)
    If projReview(cyc)>0 Then namer$=projReview(cyc)+"%" Else namer$="Unrated"
    DrawSideBox(cyc,namer$,x+362,y)
    namer$="Incomplete" 
    If projIdentity(cyc)>0 And projIdentified(cyc)=0 Then namer$="Unidentified"
    If projIdentity(cyc)>0 And projIdentified(cyc)>0 Then namer$="Unreleased"
    If projReleased(cyc)>0 Then namer$=GetFigure$(projSales(cyc))
    DrawSideBox(cyc,namer$,x+466,y)
   EndIf
  EndIf
  ;increment
  If optGrid=1
   x=x+205
   If x>rX#(400)+300 Then x=rX#(400)-205 : listY=listY+38
  Else 
   listY=listY+38
  EndIf
 Next
 ;exit option
 DrawOption(999,rX#(400),rY#(565),"<<< BACK <<<","") 
 ;confirmations
 If screenConfirm>0
  x=rX#(400) : y=rY#(300)
  DrawImage gPrompt,x,y
  SetFont font(12)
  If screenConfirm=1
   Outline("You agreed to focus on",x,y-42,0,0,0,255,255,255)
   If projIdentified(gamProject)>0 Then namer$="'"+projTitle$(gamProject)+"'" Else namer$="your current project"
   Outline(namer$+"!",x,y-8,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2
   Outline("You are on a mission to",x,y-42,0,0,0,255,255,255)
   Outline("improve your current project!",x,y-8,0,0,0,255,255,255)
  EndIf
  If screenConfirm=1 Or screenConfirm=2
   DrawOption(1002,x,y+100,"OK","") 
  Else
   DrawOption(1001,x-115,y+100,"CONFIRM","") 
   DrawOption(1002,x+115,y+100,"CANCEL","") 
  EndIf
 EndIf
 ;cursor
 If foc=>1 And foc=<no_boxes Then lastfoc=foc
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf
 If subFoc<>oldSubFoc
  oldSubFoc=subFoc
  If subFoc>0 Then PlaySound sMenuSelect
 EndIf
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
;save & exit
;SaveProgress(slot)
screen=20
End Function

;//////////////////////////////////////////////////////////////////////////////
;-------------------------------- 14. HARDWARE --------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectHardware()
;list available hardware
For count=1 To no_machines
 hardList(count)=0
 hardListed(count)=0
Next
no_boxes=0
For cyc=1 To no_machines
 leader=0 : hi=99999
 For v=1 To no_machines
  If hardReleased(v)>0 And hardReleased(v)<hi And hardListed(v)=0 Then leader=v : hi=hardReleased(v)
 Next
 If leader>0 Then no_boxes=no_boxes+1 : hardList(no_boxes)=leader : hardListed(leader)=no_boxes
Next
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
screenConfirm=0
optGrid=0 : scroll=hardScroll
If hardFoc>0 Then foc=hardFoc Else foc=no_boxes
oldfoc=foc : lastfoc=foc
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>20 Then flashTim=0   
	
	;PORTAL
	gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then PlaySound sMenuBack : go=-1 
	 If MouseDown(1) And foc=999 Then PlaySound sMenuBack : go=-1
	 ;activate
	 If MouseDown(1) And foc=>1 And foc=<no_boxes And subFoc=-1
	  ;exclusivity warning
	  If fedPolicy(charFed(gamChar))=1 And hardBrand$(hardList(foc))<>fedBrand$(charFed(gamChar)) And hardBrand$(hardList(foc))<>fedName$(charFed(gamChar))
	   PlaySound sVoid : screenConfirm=5 : keytim=20
	  Else
	 
	   If hardOwned(hardList(foc))>0
	    ;port
	    If projReleased(gamProject)>0
	     conflict=0
	     For v=1 To no_projects
	      If projReleased(v)>0 And projIdentity(v)=projIdentity(gamProject) And projFormat(v)=hardList(foc) Then conflict=1
	     Next
	     If conflict>0
	      PlaySound sVoid : screenConfirm=4 : keytim=20
	     Else
	      PlaySound sMenuGo : screenConfirm=1 : keytim=20
	     EndIf
	    Else
	     PlaySound sProduce : keytim=10
	     gamHardware=hardList(foc)
	     If projReleased(gamProject)=0 Then projFormat(gamProject)=gamHardware
	     go=1
	    EndIf
	   Else
	    ;purchase
	    If gamBank=>hardCost(hardList(foc))
	     PlaySound sMenuGo : screenConfirm=2 : keytim=20
	    Else
	     PlaySound sVoid : screenConfirm=3 : keytim=20
	    EndIf
	   EndIf
	  
	  EndIf
	 EndIf
	 ;switch display
	 If KeyDown(15) Or MouseDown(2)
	  PlaySound sMenuBrowse : keytim=10
	  If optGrid=1 Then optGrid=0 Else optGrid=1
	  scroll=0
	 EndIf
	 ;confirmations
	 If MouseDown(1) And foc=1001
	  ;port project
	  If screenConfirm=1
	   PlaySound sProduce : keytim=10
	   gamHardware=hardList(lastfoc)
	   PortProject(gamProject,gamHardware) 
	   screenConfirm=0 : go=1
	  EndIf
	  ;buy hardware
	  If screenConfirm=2
	   PlaySound sCash : keytim=10
	   gamNewHardware=hardList(lastfoc)
	   gamBank=gamBank-hardCost(gamNewHardware)
	   hardOwned(gamNewHardware)=1
	   hardFoc=lastfoc : hardScroll=scroll
	   screenConfirm=0
	   If optCutscenes>0 Then go=2
	  EndIf
	 EndIf
	 ;confirm cancel
	 If MouseDown(1) And foc=1002
	  PlaySound sMenuBack : keytim=20
	  screenConfirm=0
	 EndIf
	EndIf
	;manage music
	ManageMusic(0)
	
	;SCROLLING
	If optGrid=1
	 ;grid display
	 no_displays=(Int((rY#(525)-rY#(210))/38)+1)*3
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=5
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=5
	  EndIf
	  If scroll>0 Then scroll=0
	  excess=Int(((no_boxes-no_displays)-1)/3)+1
	  If scroll<-(excess*38) Then scroll=-(excess*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   excess=Int(((foc-no_displays)-1)/3)+1
	   If foc>no_displays And scroll>-(excess*38) Then scroll=-(excess*38)
	   recess=Int((foc-1)/3)
	   If scroll<-(recess*38) Then scroll=-(recess*38) 
	  EndIf
     EndIf 
	Else
	 ;list display
	 no_displays=Int((rY#(525)-rY#(210))/38)+1
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   scrollSpeed=4
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	  EndIf
	  If scroll>0 Then scroll=0
	  If scroll<-((no_boxes-no_displays)*38) Then scroll=-((no_boxes-no_displays)*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   If foc>no_displays And scroll>-((foc-no_displays)*38) Then scroll=-((foc-no_displays)*38)
	   If scroll<-((foc-1)*38) Then scroll=-((foc-1)*38) 
	  EndIf
     EndIf 
	EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;profile
 If lastfoc=>1 And lastfoc=<no_boxes
  DrawHardwareProfile(hardList(lastfoc),rX#(400),rY#(20),1)
 EndIf
 ;scrollers
 subFoc=-1
 If optGrid=1 Then indent=345 Else indent=345
 If no_boxes>no_displays 
  If scroll<0
   y=rY#(210)+58
   If MouseY()>y-72 And MouseY()<y+73 And screenConfirm=0
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=20
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=20
   EndIf
   If subFoc=20
    DrawImage gScroll(3),rX#(400)-indent,y
    DrawImage gScroll(3),rX#(400)+indent,y
   Else
    DrawImage gScroll(1),rX#(400)-indent,y
    DrawImage gScroll(1),rX#(400)+indent,y
   EndIf
  EndIf
  excess=Int(((no_boxes-no_displays)-1)/3)+1
  If (optGrid=0 And scroll>-((no_boxes-no_displays)*38)) Or (optGrid=1 And scroll>-(excess*38))
   y=rY#(520)-58
   If MouseY()>y-72 And MouseY()<y+73 And screenConfirm=0
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=21
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=21
   EndIf
   If subFoc=21
    DrawImage gScroll(4),rX#(400)-indent,y
    DrawImage gScroll(4),rX#(400)+indent,y
   Else
    DrawImage gScroll(2),rX#(400)-indent,y
    DrawImage gScroll(2),rX#(400)+indent,y
   EndIf
  EndIf
 EndIf
 ;HARDWARE BOXES
 foc=0
 If optGrid=0 Then x=rX#(400)-205 : listY=rY#(210)
 If optGrid=1 Then x=rX#(400)-205 : listY=rY#(210)
 For cyc=1 To no_boxes
  char=hardList(cyc)
  y=listY+scroll
  If y=>rY#(210) And y=<rY#(525)
   ;mouse hotspots
   If screenConfirm=0
    If optGrid=1
     If MouseX()=>x-101 And MouseX()=<x+101 And MouseY()=>y-15 And MouseY()=<y+18 Then foc=cyc
    Else
     If MouseX()=>x-101 And MouseX()=<x+519 And MouseY()=>y-15 And MouseY()=<y+18 Then foc=cyc
    EndIf
   EndIf
   ;name box
   If foc=cyc Then DrawImage gMenu(1),x,y Else DrawImage gMenu(2),x,y 
   If hardOwned(char)>0
    If foc=cyc Then r=0 : g=255 : b=0 Else r=100 : g=150 : b=100
   Else
    If foc=cyc Then r=255 : g=0 : b=0 Else r=150 : g=100 : b=100 
   EndIf
   Color r,g,b : Rect x-100,y-16,201,33,0
   SqueezeFont(hardTitle$(char),175,20)
   Outline(hardTitle$(char),x,y+1,r,g,b,r,g,b)
   ;additional
   If optGrid=0
    namer$=Left$(textMonth$(hardMonth(char)),3)+" "+hardYear(char)
    DrawSideBox(cyc,namer$,x+154,y)
    namer$=GetFigure$(hardUsers(char)) ;namer$=formatName$(hardFormat(char))
    DrawSideBox(cyc,namer$,x+258,y)
    namer$="$"+hardRetail(char)+" -$"+formatCost(hardFormat(char))
    DrawSideBox(cyc,namer$,x+362,y)
    If hardOwned(char)=0 Then namer$="$"+GetFigure$(hardCost(char)) Else namer$="Owned"
    DrawSideBox(cyc,namer$,x+466,y)
    Rect (x+154)-51,y-16,102,33,0
    Rect (x+258)-51,y-16,102,33,0
    Rect (x+362)-51,y-16,102,33,0
    Rect (x+466)-51,y-16,102,33,0
   EndIf
  EndIf
  ;increment
  If optGrid=1
   x=x+205
   If x>rX#(400)+300 Then x=rX#(400)-205 : listY=listY+38
  Else 
   listY=listY+38
  EndIf
 Next
 ;exit option
 DrawOption(999,rX#(400),rY#(565),"<<< BACK <<<","") 
 ;confirmations
 If screenConfirm>0
  x=rX#(400) : y=rY#(300)
  DrawImage gPrompt,x,y
  SetFont font(12)
  If screenConfirm=1
   Outline("Are you sure you want",x,y-60,0,0,0,255,255,255)
   Outline("to port '"+projTitle$(gamProject)+"'",x,y-25,0,0,0,255,255,255)
   Outline("to the "+hardTitle$(hardList(lastfoc))+"?",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2
   Outline("Are you sure you want",x,y-60,0,0,0,255,255,255)
   Outline("to buy the '"+hardTitle$(hardList(lastfoc))+"'",x,y-25,0,0,0,255,255,255)
   Outline("at a cost of $"+GetFigure$(hardCost(hardList(lastfoc)))+"?",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=3
   Outline("Your bank balance is $"+GetFigure$(gamBank)+".",x,y-60,0,0,0,255,255,255)
   Outline("You cannot afford to pay",x,y-25,0,0,0,255,255,255)
   Outline("$"+GetFigure$(hardCost(hardList(lastfoc)))+" for the '"+hardTitle$(hardList(lastfoc))+"'.",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=4
   Outline("You have already",x,y-60,0,0,0,255,255,255)
   Outline("released '"+projTitle$(gamProject)+"'",x,y-25,0,0,0,255,255,255)
   Outline("for the "+hardTitle$(hardList(lastfoc))+"!",x,y+10,0,0,0,255,255,255)
  EndIf 
  If screenConfirm=5
   Outline("You are only allowed",x,y-60,0,0,0,255,255,255)
   Outline("to develop games for",x,y-25,0,0,0,255,255,255)
   Outline(fedBrand$(charFed(gamChar))+" machines!",x,y+10,0,0,0,255,255,255)
  EndIf 
  If screenConfirm=3 Or screenConfirm=4 Or screenConfirm=5
   DrawOption(1002,x,y+100,"OK","") 
  Else
   DrawOption(1001,x-115,y+100,"CONFIRM","") 
   DrawOption(1002,x+115,y+100,"CANCEL","") 
  EndIf
 EndIf
 ;cursor
 If foc=>1 And foc=<no_boxes Then lastfoc=foc
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf
 If subFoc<>oldSubFoc
  oldSubFoc=subFoc
  If subFoc>0 Then PlaySound sMenuSelect
 EndIf
 If projReleased(gamProject)>0 Then DrawImage projBox(gamProject),MouseX()+55,MouseY()+78
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
;exit
screen=20
If go=2 Then screen=50 : screenAgenda=8
End Function


;//////////////////////////////////////////////////////////////////////////////
;---------------------------- 15. MANAGE DESKS --------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function ManageDesks()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=0 : oldfoc=0 : subfoc=0
lastfoc=1
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>40 Then flashTim=0   

	;CONFIGURATION 
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Or (MouseDown(1) And foc=19) Then PlaySound sMenuBack : go=-1 
	 ;toggle workstation
	 If MouseDown(1) And foc=>1 And foc=<6
	  screenConfirm=0
	  If foc=1 Or charRoster(gamChar,foc)>0 Then PlaySound sVoid : screenConfirm=3 : keytim=20
      If gamDesk(foc)<0 And screenConfirm=0 Then PlaySound sVoid : screenConfirm=4 : keytim=20
	  If screenConfirm=0
	   If gamDesk(foc)=0 Then gamDesk(foc)=1 : PlaySound sCash : keytim=10
	   If gamDesk(foc)=1 And keytim=0 And charRoster(gamChar,foc)=0 Then gamDesk(foc)=0 : PlaySound sProduce : keytim=10
	  EndIf
	 EndIf
	 ;go to hire
	 If MouseDown(1) And foc=>7 And foc=<12 Then PlaySound sMenuGo : go=1
	 ;repair
	 If MouseDown(1) And foc=>13 And foc=<18
	  desker=foc-12
	  If gamBank=>gamRepairCost
	   PlaySound sMenuGo : screenConfirm=1 : keytim=20  
	  Else
	   PlaySound sVoid : screenConfirm=2 : keytim=20 
	  EndIf
	 EndIf
	 ;confirmations
	 If MouseDown(1) And foc=1001
	  If screenConfirm=1
	   PlaySound sCash : gamBank=gamBank-gamRepairCost : keytim=20
	   If gamDesk(desker)=-1 Then gamDesk(desker)=-2
	   screenConfirm=0
	  EndIf
	 EndIf
	 ;confirm cancel
	 If MouseDown(1) And foc=1002
	  PlaySound sMenuBack : keytim=20
	  screenConfirm=0
	 EndIf  
	EndIf  
	;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;workstations
 foc=0
 x=110 : y=295
 For cyc=1 To 6 
  If gamDesk(cyc)>0 Then DrawImage gDesk(1),rX#(x),rY#(y) Else DrawImage gDesk(0),rX#(x),rY#(y)
  If charRoster(gamChar,cyc)>0
   If charVacant(charRoster(gamChar,cyc))>0
    DrawImage gAbsent,rX#(x)+30,rY#(y)-5
   Else 
    DrawImage charPhoto(charRoster(gamChar,cyc)),rX#(x)+30,rY#(y)-5
   EndIf
  EndIf
  If gamDesk(cyc)=-1 Then DrawImage gPiracy,rX#(x),rY#(y)
  If gamDesk(cyc)=-2 Then DrawImage gRepair,rX#(x),rY#(y)
  If MouseX()=>rX#(x)-55 And MouseX()=<rX#(x)+55 And MouseY()=>rY#(y)-60 And MouseY()=<rY#(y)+60
   If screenConfirm=0 Then foc=cyc
  EndIf
  If foc=cyc
   Color 0,0,0 : Rect rX#(x)-55,rY#(y)-60,110,120,0
   Color 255,240,0 : Rect rX#(x)-56,rY#(y)-61,112,122,0
   Color 0,0,0 : Rect rX#(x)-57,rY#(y)-62,114,124,0
  EndIf
  If gamDesk(cyc)=0 And flashTim=<20
   SetFont font(10)
   Outline("INSERT",rX#(x),rY#(y)-15,255,240,0,255,240,0)
   Outline("COIN",rX#(x),rY#(y)+15,255,240,0,255,240,0)
  EndIf
  ;upper sub-options
  If gamDesk(cyc)=-1
   If MouseX()=>rX#(x)-52 And MouseX()=<rX#(x)+52 And MouseY()=>(rY#(y)-90)-18 And MouseY()=<(rY#(y)-90)+18
    If screenConfirm=0 Then foc=12+cyc
   EndIf
   namer$="REPAIR"
   SqueezeFont(namer$,90,20)
   If foc=12+cyc
    DrawImage gMenu(3),rX#(x),rY#(y)-90
    Outline(namer$,rX#(x),rY#(y)-88,255,240,0,255,240,0)
   Else
    DrawImage gMenu(4),rX#(x),rY#(y)-90
    Outline(namer$,rX#(x),rY#(y)-88,175,175,175,175,175,175)
   EndIf
  EndIf
  ;lower sub-options
  If cyc>1 And gamDesk(cyc)<>0 And charRoster(gamChar,cyc)=0
   If MouseX()=>rX#(x)-52 And MouseX()=<rX#(x)+52 And MouseY()=>(rY#(y)+90)-18 And MouseY()=<(rY#(y)+90)+18
    If screenConfirm=0 Then foc=6+cyc
   EndIf
   namer$="HIRE"
   SqueezeFont(namer$,90,20)
   If foc=6+cyc
    DrawImage gMenu(3),rX#(x),rY#(y)+90
    Outline(namer$,rX#(x),rY#(y)+92,255,240,0,255,240,0)
   Else
    DrawImage gMenu(4),rX#(x),rY#(y)+90
    Outline(namer$,rX#(x),rY#(y)+92,175,175,175,175,175,175)
   EndIf
  EndIf
  x=x+120
 Next 
 ;profile
 If foc=>1 And foc=<6
  If charRoster(gamChar,foc)>0 Then lastfoc=foc
 EndIf
 If foc=>7 And foc=<12
  If charRoster(gamChar,foc-6)>0 Then lastfoc=foc-6
 EndIf
 If foc=>13 And foc=<18
  If charRoster(gamChar,foc-12)>0 Then lastfoc=foc-12
 EndIf 
 DrawCharProfile(charRoster(gamChar,lastfoc),rX#(400),rY#(20),1)
 ;cost estimate
 SetFont font(15)
 OutlineStraight("Rent =",rX#(400)-171,rY#(435),0,0,0,255,240,0)
 namer$=CountDesks()+" x $"+GetFigure$(gamRent)+" per "+textPeriod$(optTime) 
 If fedPolicy(charFed(gamChar))=9 Then namer$="Subsidised by "+fedName$(charFed(gamChar))
 OutlineStraight(namer$,rX#(400)-60,rY#(435),0,0,0,255,255,255)
 OutlineStraight("Wages =",rX#(400)-187,rY#(475),0,0,0,255,240,0) 
 OutlineStraight("$"+GetFigure$(CalculateWages())+" per "+textPeriod$(optTime),rX#(400)-60,rY#(475),0,0,0,255,255,255)
 ;options
 DrawOption(19,rX#(400),rY#(565),"<<< BACK <<<","") 
 ;confirmations
 If screenConfirm>0
  x=rX#(400) : y=rY#(300)
  DrawImage gPrompt,x,y
  SetFont font(12)
  If screenConfirm=1
   Outline("Are you sure you want",x,y-60,0,0,0,255,255,255)
   Outline("to pay $"+GetFigure$(gamRepairCost)+" to repair",x,y-25,0,0,0,255,255,255)
   Outline("this workstation?",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2
   Outline("You cannot afford to",x,y-60,0,0,0,255,255,255)
   Outline("pay $"+GetFigure$(gamRepairCost)+" to repair",x,y-25,0,0,0,255,255,255)
   Outline("this workstation!",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=3
   Outline("You cannot close this",x,y-60,0,0,0,255,255,255)
   Outline("workstation while it",x,y-25,0,0,0,255,255,255)
   Outline("is still being used!",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=4
   Outline("You cannot close this",x,y-60,0,0,0,255,255,255)
   Outline("workstation until it",x,y-25,0,0,0,255,255,255)
   Outline("has been repaired!",x,y+10,0,0,0,255,255,255)
  EndIf
  If screenConfirm=2 Or screenConfirm=3 Or screenConfirm=4
   DrawOption(1002,x,y+100,"OK","") 
  Else
   DrawOption(1001,x-115,y+100,"CONFIRM","") 
   DrawOption(1002,x+115,y+100,"CANCEL","") 
  EndIf
 EndIf
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
screen=20
If go=1 Then screen=11 : screenAgenda=3
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;GENERATE CAREER
Function GenerateCareer()
 ;randomize rosters
 If optJumble>0
  JumbleRosters()
 EndIf
 ;get character slot
 Repeat
  gamChar=Rnd(1,no_chars)
 Until charFed(gamChar)=no_feds And gamChar<>fedBooker(charFed(gamChar)) And charName$(gamChar)<>"Mat Dickie"
 ;default stats
 charDesign(gamChar)=25 : charProgram(gamChar)=25
 char2D(gamChar)=25 : char3D(gamChar)=25
 charSound(gamChar)=25 : charMusic(gamChar)=25
 While (charDesign(gamChar)+charProgram(gamChar)+char2D(gamChar)+char3D(gamChar)+charSound(gamChar)+charMusic(gamChar))<270
  randy=Rnd(1,6)
  If randy=1 Then charDesign(gamChar)=charDesign(gamChar)+5
  If randy=2 Then charProgram(gamChar)=charProgram(gamChar)+5
  If randy=3 Then char2D(gamChar)=char2D(gamChar)+5
  If randy=4 Then char3D(gamChar)=char3D(gamChar)+5
  If randy=5 Then charSound(gamChar)=charSound(gamChar)+5
  If randy=6 Then charMusic(gamChar)=charMusic(gamChar)+5 
 Wend
 charRep(gamChar)=30 : charHappiness(gamChar)=0
 charContract(gamChar)=0 : charSalary(gamChar)=0 : charRoyalties(gamChar)=50
 ;reset status
 gamDate=optStartDate
 gamMission=0 : gamTarget=0 : gamDeadline=0
 gamBank=100000 : gamMillionaire=0
 If GetYear(gamDate)>1981 Then gamBank=gamBank+((GetYear(gamDate)-1981)*5000)
 gamTax=35 : gamRent=1000 : gamRepairCost=1000 : gamFashion=1
 For count=1 To 10
  gamHiScore(count)=0
  gamHiCredit(count)=0
 Next
 For count=1 To 50
  gamAgreement(count)=0
 Next
 ;reset hardware
 For cyc=1 To no_machines
  hardReleased(cyc)=0 : hardOwned(cyc)=0
  hardUsers(cyc)=0 : hardPromotion#(cyc)=0 : hardOldPromotion#(cyc)=0
  date=GetDate(hardWeek(cyc),hardMonth(cyc),hardYear(cyc))
  If date=<gamDate
   hardReleased(cyc)=date
   If cyc=1 Then hardUsers(cyc)=1000000 Else hardUsers(cyc)=Rnd(100000,2000000)
   ;If cyc=18 Or cyc=36 Then hardUsers(cyc)=hardUsers(cyc)*2
   For count=1 To 30
    If GetYear(gamDate)=>GetYear(date)+count Then hardPromotion#(cyc)=hardPromotion#(cyc)-10
   Next
   If (cyc=18 Or cyc=36) And hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0
   If hardPromotion#(cyc)<-99 Then hardPromotion#(cyc)=-99
   formatCost(hardFormat(cyc))=formatCost(hardFormat(cyc))+Rnd(-1,1)
   hardRetail(cyc)=hardRetail(cyc)+Rnd(-1,1)
  EndIf
  hardPeakUsers(cyc)=hardUsers(cyc) 
  hardOldGameplay(cyc)=hardGameplay(cyc)
  hardOldReplay(cyc)=hardReplay(cyc)
  hardOld2D(cyc)=hard2D(cyc)
  hardOld3D(cyc)=hard3D(cyc)
  hardOldSound(cyc)=hardSound(cyc)
  hardOldMusic(cyc)=hardMusic(cyc)
 Next
 gamHardware=1 : hardOwned(1)=1
 If hardReleased(11)>0 Then gamHardware=11
 If hardReleased(18)>0 Then gamHardware=18
 hardOwned(gamHardware)=1
 If CountMachines(0)=1 Then hardPromotion#(1)=1
 FindIndustryStandards()
 hardLeader(0)=gamHardware
 ;reset projects
 For cyc=1 To 100
  ResetProject(cyc)
 Next
 no_projects=1 : gamProject=1
 projTitle$(gamProject)="Project #"+Dig$(gamProject,10)
 LoadProjectImages() 
 gamRelease=0
 ;reset roster
 For count=1 To optTeamSize
  charRoster(gamChar,count)=0
  gamDesk(count)=0
 Next
 charTeam(gamChar)=gamChar
 charRoster(gamChar,1)=gamChar
 gamDesk(1)=1 : gamDeskPosition=Rnd(1,6)
 ;reset characters
 For char=1 To no_chars
  If char<>gamChar And charTeam(char)=gamChar Then charTeam(char)=0
  ChangeRelationship(char,gamChar,0)
  charNegotiated(char)=0
  If gamInherit=0
   If char<>gamChar 
    ager=2011-GetYear(gamDate)
    If charAge(char)=>18+ager
     charAge(char)=charAge(char)-ager
    Else
     If charAge(char)=>18+(ager/2) Then charAge(char)=charAge(char)-(ager/2)
    EndIf
    GenerateContract(char)
    If charContract(char)>0 And char<>fedBooker(charFed(char)) Then charContract(char)=Rnd(0,48)
    charHappiness(char)=Rnd(-2,2)
   EndIf
  EndIf
  For label=0 To no_feds
   charExperience(char,label)=0
  Next
  ;If charExperience(char,charFed(char))<1 Then charExperience(char,charFed(char))=1
  For count=1 To 50
   charAgreement(char,count)=0
  Next
  ResetOldValues(char)
 Next
 FindLeadingCharacters()
 ;reset companies
 For label=1 To no_feds
  fedOldPopularity(label)=fedPopularity(label)
  fedOldReputation(label)=fedReputation(label)
  fedPolicy(label)=Rnd(-4,no_policies)
  If fedPolicy(label)<0 Or label=no_feds Then fedPolicy(label)=0 
  fedBrand$(label)="Atari"
 Next
 ;generate charts
 UpdateCharts(1)
 ;new office plants
 For cyc=1 To 12
  deskPlant(cyc)=Rnd(1,no_plants)
 Next
End Function

;RESET SCHEDULE
Function ResetSchedule(start)
 ;negate missions
 gamMission=0 : gamTarget=0 : gamDeadline=0
End Function

;FORMULATE DATE (into one number)
Function GetDate(week,month,year)
 value=(year*48)+(month*4)+week
 Return value
End Function

;CALCULATE REAL DATE!
Function GetRealDate()
 ;identify week
 weeker=Int(Left$(CurrentDate$(),2))
 If weeker=<7 Then week=1
 If weeker=>8 And weeker=<15 Then week=2
 If weeker=>16 And weeker=<23 Then week=3
 If weeker>23 Then week=4
 ;identify month
 For count=1 To 12
  monther$=Mid$(CurrentDate$(),4,3)
  If monther$=Left$(textMonth$(count),3) Then month=count
 Next
 ;identify year
 year=Int(Right$(CurrentDate$(),4))
 ;translate to game
 gamDate=GetDate(month,week,year)
End Function

;EXTRACT YEAR FROM DATE
Function GetYear(date)
 date=date-5
 value=Floor(date/48)
 If value<0 Then value=0
 Return value
End Function

;EXTRACT MONTH FROM DATE
Function GetMonth(date)
 date=date-(GetYear(date)*48)
 date=date-1
 value=Floor(date/4)
 If value<1 Then value=1
 If value>12 Then value=12
 Return value
End Function

;EXTRACT WEEK FROM DATE
Function GetWeek(date)
 value=(date-(GetYear(date)*48))-(GetMonth(date)*4)
 If value<1 Then value=1
 If value>4 Then value=4
 Return value
End Function

;DESCRIBE DATE
Function DescribeDate$(date,detail) ;1=include year
 dateline$=textMonth$(GetMonth(date))
 If optTime=1 Then dateline$=textWeek$(GetWeek(date))+" of "+dateline$
 If detail>0 Then dateline$=dateline$+" "+GetYear(date)
 Return dateline$
End Function

;COUNT CAREER EXPERIENCE
Function CountExperience(char,label)
 value=0
 If label>0
  value=charExperience(char,label)
 Else
  For count=1 To no_feds
   value=value+charExperience(char,count)
  Next
 EndIf
 Return value
End Function

;COUNT RELATIONSHIPS
Function CountRelationships(char,task) ;-1=enemies, 1=friends
 value=0
 For v=1 To no_chars
  If char<>v
   If task>0 And charRelationship(char,v)>0 Then value=value+1
   If task<0 And charRelationship(char,v)<0 Then value=value+1
  EndIf
 Next 
 Return value
End Function

;CALCULATE WAGES
Function CalculateWages()
 value=0
 For v=1 To optTeamSize
  If charRoster(gamChar,v)>0 And charRoster(gamChar,v)<>gamChar Then value=value+charSalary(charRoster(gamChar,v))
 Next 
 Return value
End Function

;DISPLAY BANK BALANCE
Function DisplayMoney(amount,x,y)
 DrawImage gMoney,x,y
 SetFont fontNews(6)
 If amount>0 Then r=100 : g=200 : b=100 : Else r=200 : g=100 : b=100
 Outline("$"+GetFigure$(amount),x,y+30,0,0,0,r,g,b)
End Function

;DISPLAY CALENDAR
Function DisplayCalendar(date,x,y)
 DrawImage gCalendar,x,y
 If optTime=1
  SetFont fontNews(1)
  Color 255,255,255 : Text x-42,y-19,textWeek$(GetWeek(date)),0,1
 EndIf
 SetFont fontNews(3)
 Color 0,0,0 : Text x,y+1,textMonth$(GetMonth(date)),1,1
 SetFont fontNews(6)
 Color 0,0,0 : Text x,y+23,GetYear(date),1,1
End Function