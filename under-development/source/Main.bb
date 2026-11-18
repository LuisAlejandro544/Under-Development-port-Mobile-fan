;-------------------------------------------------------------------------
;////////////////////////// UNDER DEVELOPMENT ////////////////////////////
;-------------------------------------------------------------------------
;~~~~~~~~~~~~~~~~~~~~~ Copyright © Mat Dickie 2011 ~~~~~~~~~~~~~~~~~~~~~~~
;~~~~~~~~~~ This program may not be re-released under any other ~~~~~~~~~~
;~~~~~~~ identity or sold commercially without express permission. ~~~~~~~
;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;FILE STRUCTURE
SeedRnd(MilliSecs())
AppTitle "Under Development"
Include "Values.bb"
Include "Texts.bb"
Include "Hardware.bb"
Include "Software.bb"
Include "Functions.bb"
Include "Data.bb"
Include "Menus.bb"
Include "Credits.bb"
Include "Rosters.bb"
Include "Editor.bb"
Include "World.bb"
Include "Promos.bb"
Include "Career.bb"
Include "Reviews.bb"
Include "News.bb"
Include "Charts.bb"
Include "Meetings.bb"
Include "Negotiations.bb"
;subgames
Include "Subgames/Invaders.bb"
Include "Subgames/Tetris.bb"
Include "Subgames/Breakout.bb"
Include "Subgames/Asteroids.bb"
Include "Subgames/Moksha.bb"
Include "Subgames/Snake.bb"
Include "Subgames/SureShot.bb"
Include "Subgames/Crosshair.bb"

;INITIATE ENGINE 
LoadOptions()
ChangeResolution(optMenuRes,0)
SetBuffer BackBuffer()
AutoMidHandle True
EnableDirectInput True
HidePointer
MoveMouse rX#(650),rY#(500)
SeedRnd(MilliSecs())   

;LOADING PROCESS
;start intro
Intro()
;load media
LoadMusic()
LoadImages()
LoadTextures()
;default universe
Loader("Please Wait","Restoring Data")
LoadUniverse()
slot=0
LoadProgress(slot)
LoadWorld(slot)
LoadChars(slot)
LoadPhotos(slot)
;software data
Loader("Please Wait","Loading Software")
LoadGameData()
LoadGameBox(0) : LoadGameScreen(0)
LoadGameBox(418) : LoadGameScreen(418)
;restore normal music
ManageMusic(Rnd(1,no_themes))

;SCREEN MANAGEMENT
SeedRnd(MilliSecs())
screen=1 
Repeat
 ;load screen
 If screen>0
  LoadScreen(screen)
 EndIf
 ;get-out clause
 If KeyDown(56) And KeyDown(45) Then End
Until screen=0
End 

;SCREEN ACCESS
Function LoadScreen(request)
 ;main menus
 If request=1 Then MainMenu()
 If request=2 Then Options()
 If request=3 Then Credits()
 If request=4 Then Outro()
 ;setup menus
 If request=10 Then SelectSlot()
 If request=11 Then SelectRoster()
 If request=12 Then SelectCharacter()
 If request=13 Then SelectProject()
 If request=14 Then SelectHardware()
 If request=15 Then ManageDesks()
 If request=16 Then SelectGame()
 If request=17 Then SoftwareLibrary()
 If request=18 Then EditGame()
 ;career screens
 If request=20 Then CareerHub()
 
 If request=22 Then IntroduceDate() 
 If request=23 Then NewsReports()
 If request=24 Then FinanceReport()
 If request=25 Then Retirement()
 If request=26 Then HallOfFame()
 If request=27 Then ConfirmUniverse()
 If request=28 Then GameChart()
 If request=29 Then Review()
 ;3D scenes
 If request=50 Then OfficeWork()
 If request=51 Then Editor()
 If request=52 Then NegotiateDeal()
 If request=53 Then Meeting()
 If request=54 Then RecruitMember()
 ;sub-games
 If request=61 Then Tetris()
 If request=62 Then SpaceInvaders()
 If request=63 Then Breakout()
 If request=64 Then Asteroids()
 If request=65 Then Moksha()
 If request=66 Then Snake() 
 If request=67 Then SureShot() 
 If request=68 Then CrossHair() 
End Function

;DELIBERATE CRASH LOOPHOLE
Function LoopHole()
 LoopHole()
End Function

;----------------------------------------------------------------------------
;/////////////////////////// 50. OFFICE WORK ////////////////////////////////
;----------------------------------------------------------------------------
Function OfficeWork()
;loading message
Loader("Please Wait","Entering Office")
;chThemeVol#=0.5 : ChannelVolume chTheme,chThemeVol#
;load office
fed=charFed(gamChar) : negSetting=6 
PrepareStudio()
If screenAgenda=0 Or screenAgenda=8 Then camFoc=negStar : camType=2
If screenAgenda=2 Then camFoc=negStar : camType=1
If screenAgenda=4 Then camAccel=0
For cyc=1 To no_plays
 pFoc(cyc)=0
Next
;alternative identities
If screenAgenda=6
 camFoc=negStar : camType=2
 For cyc=1 To 3
  projPossibleID(cyc)=0
 Next
 If projIdentity(gamProject)>0 Then projPossibleID(Rnd(1,3))=projIdentity(gamProject)
 For cyc=1 To 3
  Loader("","") 
  If projPossibleID(cyc)=0
   Repeat
    projPossibleID(cyc)=Rnd(1,no_games)
   Until projPossibleID(cyc)<>projIdentity(gamProject)
  EndIf
  projPossibleBox(cyc)=LoadImage("Games/Box Art/Box"+Dig$(projPossibleID(cyc),100)+".jpg")
  ResizeImage projPossibleBox(cyc),100,145
  MaskImage projPossibleBox(cyc),255,0,255
 Next
EndIf
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
screenState=0
foc=0 : oldfoc=foc
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;CONFIGURATION
	gotim=gotim+1
	keytim=keytim-1
	If keytim<0 Then keytim=0
	;viewing options
	If screenAgenda=0 Or screenAgenda=8
	 If KeyDown(1) Or KeyDown(28) Or MouseDown(2) Then PlaySound sMenuBack : go=-1
	EndIf
	If gotim=20 And screenAgenda=8 Then PlaySound sMilestone
	;development menu
	If screenAgenda=2 And screenState=0
	 camType=1
	 If gotim>20 And keytim=0
	  If KeyDown(1) Or (MouseDown(1) And foc=8) Then PlaySound sMenuBack : go=-1
	  If MouseDown(1) And foc=>1 And foc=<7
	   PlaySound sMenuBrowse : keytim=20
	   screenState=1 : gotim=0
	   ;If TeamSize(gamChar)=1 And charWorked(gamChar)=0
	    ;PlaySound sMenuGo : screenState=2
	    ;camFoc=negStar : gamWorker=pChar(camFoc) : pAnim(camFoc)=10
	   ;EndIf
	   gamDevelop=foc
	   If gamDevelop=>1 And gamDevelop=<6 And projLocked(gamProject,gamDevelop)>0
	    For v=1 To no_plays
	     If pChar(v)=projCredit(gamProject,gamDevelop) And pChar(v)<>gamChar Then screenState=-1 : gotim=0 : camFoc=v : negTopic=1 : negTim=0
	    Next
	   EndIf
	  EndIf
	 EndIf
	EndIf 
	;character selection
	If screenAgenda=2 And screenState=1
	 camType=1
	 If gotim>20 And keytim=0
	  If KeyDown(1) Or (MouseDown(1) And foc=4) Then PlaySound sMenuBrowse : keytim=20 : screenState=0 : gotim=10
	  If MouseDown(1) And foc=1
	   PlaySound sMenuBrowse : keytim=10
	   Repeat
	    camFoc=camFoc-1
	    If camFoc<1 Then camFoc=no_plays
	   Until gamDesk(camFoc)<>0
	  EndIf
	  If MouseDown(1) And foc=2
	   PlaySound sMenuBrowse : keytim=10
	   Repeat
	    camFoc=camFoc+1
	    If camFoc>no_plays Then camFoc=1
	   Until gamDesk(camFoc)<>0
	  EndIf
	  ;confirm
	  If MouseDown(1) And (foc=3 Or foc=5)
	   PlaySound sMenuGo : keytim=20
	   screenState=2 : gotim=0
	   If foc=5 Then optAuto=1 Else optAuto=0
	   gamWorker=pChar(camFoc)
	   ;dedicated role
	   If gamDevelop=>1 And gamDevelop=<6 And charDedicated(gamWorker)>0 And charDedicated(gamWorker)<>gamDevelop And gamWorker<>gamChar
	    screenState=-1 : gotim=0 : negTopic=9 : negTim=0
	   EndIf
	   ;alaready worked
	   If charWorked(gamWorker)>0
	    If pAnim(camFoc)=<10 Then pAnim(camFoc)=11
	    screenState=-1 : gotim=0 : negTopic=2 : negTim=0
	    If gamWorker=gamChar Then PlaySound sVoid : screenState=1 : gotim=100
	   Else
	    If pAnim(camFoc)=<9 Then pAnim(camFoc)=10
	   EndIf
	   ;broken equipment
	   If gamDesk(camFoc)<0
	    screenState=-1 : gotim=0 : negTopic=6 : negTim=0
	    If pAnim(camFoc)=<10 Then pAnim(camFoc)=5
	   EndIf
	   ;update screen
	   If screenState=2 And projIdentity(gamProject)=<0
	    If gamDevelop=>1 And gamDevelop=<6 
	     TextureScreens(camFoc,tDevelop(gamDevelop))
	    Else
	     TextureScreens(camFoc,tDevelop(Rnd(1,6)))
	    EndIf
	   EndIf
	
	  EndIf
	 EndIf
	EndIf  
	;zoom into game
	If screenAgenda=2 And screenState=2
	 camType=3
	 If gotim=0 Then PlaySound sTyping
	 If GetDistance#(camX#,camZ#,camPivTX#,camPivTZ#)<7.5
	  If optAuto>0
	   SimulateProgress()
	   ApplyNightLight()
	   pAnim(camFoc)=Rnd(1,9)
	   If projIdentity(gamProject)=>0
  		tScreenshot=LoadTexture("Games/Screenshots/Screen"+Dig$(projIdentity(gamProject),100)+".JPG")
        TextureScreens(camFoc,tScreenshot) 
 	   Else
  	    TextureScreens(camFoc,tDevelop(Abs(projIdentity(gamProject))))
 	   EndIf 
	   If hardHandheld(gamHardware)>0 And projIdentity(gamProject)>0
	  	pAnim(camFoc)=15+hardHandheld(gamHardware)
	    ApplyHandhelds(camFoc)
   	    HideEntity FindChild(desk(camFoc),hardModel$(gamHardware))
   	    HideEntity FindChild(desk(camFoc),hardTV$(gamHardware)+"S")
    	If FindChild(desk(camFoc),hardCom$(gamHardware)+"S")>0
         HideEntity FindChild(desk(camFoc),hardCom$(gamHardware)+"S")
        EndIf
 	   EndIf
	   camAccel=0
	   screen=50 : screenAgenda=4 : screenState=0 : gotim=1
	  Else
	   go=1
	  EndIf
	 EndIf
	EndIf
	;aftermath
	If screenAgenda=4
	 camType=2
	 If screenState=0 And gotim=20
	  If projIdentity(gamProject)>0 And projOldIdentity(gamProject)=<0 Then PlaySound sMilestone
	 EndIf
	 If screenState=0 And gotim>100 Then screenState=1 : gotim=0
	 If screenState=1 And keytim=0
	  v=gamProject
	  If projGameplay(v)<projNewGameplay(v) Then projGameplay(v)=projGameplay(v)+1 : keytim=4
	  If projGameplay(v)>projNewGameplay(v) Then projGameplay(v)=projGameplay(v)-1 : keytim=4
	  If projReplay(v)<projNewReplay(v) Then projReplay(v)=projReplay(v)+1 : keytim=4
	  If projReplay(v)>projNewReplay(v) Then projReplay(v)=projReplay(v)-1 : keytim=4
	  If proj2D(v)<projNew2D(v) Then proj2D(v)=proj2D(v)+1 : keytim=4
	  If proj2D(v)>projNew2D(v) Then proj2D(v)=proj2D(v)-1 : keytim=4
	  If proj3D(v)<projNew3D(v) Then proj3D(v)=proj3D(v)+1 : keytim=4
	  If proj3D(v)>projNew3D(v) Then proj3D(v)=proj3D(v)-1 : keytim=4
	  If projSound(v)<projNewSound(v) Then projSound(v)=projSound(v)+1 : keytim=4
	  If projSound(v)>projNewSound(v) Then projSound(v)=projSound(v)-1 : keytim=4
	  If projMusic(v)<projNewMusic(v) Then projMusic(v)=projMusic(v)+1 : keytim=4
	  If projMusic(v)>projNewMusic(v) Then projMusic(v)=projMusic(v)-1 : keytim=4
	  If keytim=4 Then PlaySound sMenuBrowse
	  If projGameplay(v)=projNewGameplay(v) And projReplay(v)=projNewReplay(v)
	   If proj2D(v)=projNew2D(v) And proj3D(v)=projNew3D(v)
	    If projSound(v)=projNewSound(v) And projMusic(v)=projNewMusic(v) Then screenState=2 : gotim=0
	   EndIf
	  EndIf
	 EndIf
	 If screenState=2
	  camType=4
	  If gotim>200 Or (gotim>100 And MouseDown(1)) Then screenAgenda=2 : screenState=0 : gotim=0 : keytim=20 
	 EndIf
	EndIf
	;identification process
	If screenAgenda=6 
	 camType=2 
	 If screenState=0 And gotim>100
	  screenState=-1 : gotim=0 : negTopic=3 : negTim=0 : camFoc=negVisitor
	  ;If gamChar=fedBooker(fed) Then screenState=1 : camFoc=negStar
	  If fedPolicy(fed)=6 And projIdentified(gamProject)>0
	   If AverageProjectStats(gamProject,1)<AverageHardwareStats(gamHardware)-10 Then negTopic=10 
	  EndIf
	  If projIdentified(gamProject)>0 And gamMission=>1 And gamMission=<6 Then negTopic=11 
	  If projIdentity(gamProject)=<0 Or (projIdentified(gamProject)>0 And projIdentity(gamProject)<>projIdentified(gamProject)) Then negTopic=7
	  If projIdentified(gamProject)<0 Then negTopic=8
	 EndIf
	 If screenState=1 And gotim>20 And keytim=0
	  If KeyDown(1) Then PlaySound sMenuBack : go=-1
	  If MouseDown(1) And foc=>1 And foc=<3
	   If projPossibleID(foc)=projIdentity(gamProject)
	    PlaySound sMilestone : keytim=10
	    projIdentified(gamProject)=projIdentity(gamProject)
	    projTitle$(gamProject)=softTitle$(projIdentity(gamProject))
	    If softDated(projIdentity(gamProject))>0 Then projTitle$(gamProject)=projTitle$(gamProject)+" "+GetYear(gamDate)
	    If hardHandheld(gamHardware)>0
	     HideEntity FindChild(p(negStar),hardModel$(gamHardware))
	    EndIf
	    pAnim(negStar)=15 : pAnim(negVisitor)=25
	    screenState=-1 : gotim=0 : negTopic=4 : negTim=0 : camFoc=negVisitor
	    If gamChar=fedBooker(fed) Then screenState=2 : camFoc=negStar
	   Else
	    PlaySound sIncorrect : keytim=10
	    projIdentified(gamProject)=-1
	    screenState=-1 : gotim=0 : negTopic=5 : negTim=0 : camFoc=negVisitor
	    If gamChar=fedBooker(fed) Then screenState=2 : camFoc=negStar
	   EndIf
	  EndIf
	 EndIf 
	 If screenState=2
	  If gotim>200 Or (gotim>100 And negTopic<>4) Then camType=4 
	  If gotim>400 Or (gotim>200 And negTopic<>4) Then go=1
	 EndIf
	EndIf 
	;speeches
	If screenState=-1
	 camType=10
	 negTim=negTim+1
	 If gotim>20 And keytim=0
	  If KeyDown(1) Or MouseDown(1) Then negTim=negTim+50 : keytim=5
	 EndIf
	EndIf 
	;decoration
	If KeyDown(56) And KeyDown(57) And keytim=0 Then DecorateOffice(Rnd(1,9)) : keytim=10
	
	;ACTORS
	For cyc=1 To no_plays
	 If pChar(cyc)>0
	  ;look at choice
	  If screenAgenda=2 And screenState=1
       If pChar(camFoc)>0 And camFoc<>cyc Then pFoc(cyc)=camFoc Else pFoc(cyc)=0
      EndIf
      ;look at screen
	  If screenAgenda=6 And screenState=>0 Then pFoc(cyc)=0
      ;change animation
      If screenAgenda<>2 Or screenState<>2 Then RandomizeAnimation(cyc)
      If pAnim(cyc)=15 Or pAnim(cyc)=25 Then ShowEntity FindChild(p(cyc),"Box")
	  ;facial expressions
	  FacialExpressions(cyc)
	  ;shadows
	  For limb=1 To 50
       If pShadow(cyc,limb)>0
        RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
        PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
       EndIf
      Next
     EndIf 
	Next
	
	;CAMERA
	Camera() 
	;manage music
	ManageMusic(0)  
	
 UpdateWorld
 Next  
 ;animation override
 For cyc=1 To no_plays
  If pChar(cyc)>0
   target=0
   If cyc=<optTeamSize Then target=FindChild(desk(cyc),hardTV$(deskHardware(cyc))+"S")
   If (pAnim(cyc)=>16 And pAnim(cyc)=<18) Or (pAnim(cyc)=>26 And pAnim(cyc)=<28) Then target=FindChild(p(cyc),hardModel$(deskHardware(cyc))+"S")
   If screenAgenda=6 Or screenAgenda=8 Or cyc>optTeamSize
    target=FindChild(desk(negStar),hardTV$(deskHardware(negStar))+"S")
    If (pAnim(negStar)=>16 And pAnim(negStar)=<18) Or (pAnim(negStar)=>26 And pAnim(negStar)=<28) Then target=FindChild(p(negStar),hardModel$(deskHardware(negStar))+"S")
    If pAnim(negStar)=15 Or pAnim(negStar)=25 Then target=FindChild(p(negStar),"Box")
   EndIf
   If (pAnim(cyc)=>16 And pAnim(cyc)=<18) Or (pAnim(cyc)=>26 And pAnim(cyc)=<28) Then target=0
   If pFoc(cyc)>0 And pChar(pFoc(cyc))>0 Then target=FindChild(p(pFoc(cyc)),"Head")
   If screenAgenda=8 And hardHandheld(gamNewHardware)=0 Then target=FindChild(desk(negStar),hardModel$(deskHardware(negStar)))
   If screenAgenda=6 And (pAnim(cyc)=15 Or pAnim(cyc)=25) Then target=0
   If pAnim(cyc)=11 Then target=0
   If target>0
    PointHead(cyc,target)
   EndIf
  EndIf
 Next

 RenderWorld 1

 ;DISPLAY
 ;reset expressions
 foc=0
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;development menu
 If screenAgenda=2 And screenState=0
  x=400 : y=220 : startTim=20
  If gotim>startTim+3 Then DrawOption(1,rX#(x),rY#(y),"DESIGN","") : DrawImage gIconLarge(1),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+6 Then DrawOption(2,rX#(x),rY#(y),"PROGRAMMING","") : DrawImage gIconLarge(2),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+9 Then DrawOption(3,rX#(x),rY#(y),"2D GRAPHICS","") : DrawImage gIconLarge(3),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+12 Then DrawOption(4,rX#(x),rY#(y),"3D GRAPHICS","") : DrawImage gIconLarge(4),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+15 Then DrawOption(5,rX#(x),rY#(y),"SOUND","") : DrawImage gIconLarge(5),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+18 Then DrawOption(6,rX#(x),rY#(y),"MUSIC","") : DrawImage gIconLarge(6),rX#(x)-100,rY#(y) : y=y+45
  If gotim>startTim+21 Then DrawOption(7,rX#(x),rY#(y),"EVERYTHING","") : y=y+60
  If gotim>startTim+24 Then DrawOption(8,rX#(400),rY#(565),"<<< BACK <<<","")
  If gotim>startTim Then DrawProjectProfile(gamProject,rX#(400),rY#(15),1)
 EndIf
 ;character selection
 If screenAgenda=2 And screenState=1 And gotim>10
  DrawOption(1,rX#(125),rY#(300),"<<< PREVIOUS <<<","")
  DrawOption(2,rX#(675),rY#(300),">>> NEXT >>>","")
  available=1
  If pChar(camFoc)=gamChar And charWorked(gamChar)>0 Then available=0
  If charRoster(gamChar,camFoc)=0 Or charVacant(charRoster(gamChar,camFoc))>0 Then available=0
  If available=1 
   DrawOption(3,rX#(400),rY#(475),"PLAY","")
   DrawOption(5,rX#(400),rY#(515),"SIMULATE","")
   If gamDevelop=>1 And gamDevelop=<6 Then DrawImage gIconLarge(gamDevelop),rX#(400)-100,rY#(495) 
   DrawImage gControl(2),rX#(400)+100,rY#(475)
   DrawImage gFormat(0),rX#(400)+100,rY#(515)
  EndIf
  DrawOption(4,rX#(400),rY#(565),"<<< BACK <<<","")
  If charRoster(gamChar,camFoc)>0
   DrawCharProfile(charRoster(gamChar,camFoc),rX#(400),rY#(10),1)
  EndIf
 EndIf
 ;aftermath
 If screenAgenda=4 And screenState=>1
  DrawCharProfile(gamWorker,rX#(400),rY#(10),1)
  DrawProjectProfile(gamProject,rX#(400)-60,rY#(600)-175,0)
  SetFont font(14)
  r=255 : g=255 : b=255
  If gamIncrease>0 Then r=100 : g=200 : b=100
  If gamIncrease<0 Then r=200 : g=100 : b=100
  Outline(GetFigure$(gamScore(0))+"pts @ "+gamSkill+"% skill",rX#(400)-200,rY#(600)-115,0,0,0,r,g,b)
  If gamIncrease=>0 Then Outline("= "+gamIncrease+"% increase",rX#(400)-200,rY#(600)-80,0,0,0,r,g,b)
  If gamIncrease<0 Then Outline("= "+gamIncrease+"% decrease",rX#(400)-200,rY#(600)-80,0,0,0,r,g,b)
  SetFont font(10)
  If gamPerfect>0 Then Outline("(Perfectionism)",rX#(400)-200,rY#(600)-50,0,0,0,r,g,b)
 EndIf
 ;identification choices
 If screenAgenda=6 And screenState=1
  x=700 : y=100
  For cyc=1 To 3
   If gotim>20+(cyc*5)
    DrawImage projPossibleBox(cyc),rX#(x),rY#(y)
    If MouseX()=>rX#(x)-50 And MouseX()=<rX#(x)+50 And MouseY()=>rY#(y)-73 And MouseY()=<rY#(y)+73 Then foc=cyc
    If foc=cyc
     Color 0,0,0 : Rect rX#(x)-50,rY#(y)-73,100,146,0
     Color 255,240,0 : Rect rX#(x)-51,rY#(y)-74,102,148,0
     Color 0,0,0 : Rect rX#(x)-52,rY#(y)-75,104,150,0
    EndIf
   EndIf
   y=y+200
  Next
 EndIf
 ;speeches
 If screenState=-1
  lineA$="" : lineB$=""
  redLineA$="" : redLineB$=""
  greenLineA$="" : greenLineB$=""
  If negTopic=1
   If negTim>25 And negTim<325
    Speak(camFoc,0,1)
    lineA$="I already did a good job on the "+textAttribute$(gamDevelop)+","
    lineB$=charName$(gamChar)+"! What do you want to change it for?"
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=0 : gotim=10
  EndIf
  If negTopic=2
   If negTim>25 And negTim<325
    Speak(camFoc,0,1)
    lineA$="I've already done enough work for one day,"
    lineB$=charName$(gamChar)+"! Find somebody else to do it..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=1 : gotim=0
  EndIf
  If negTopic=3
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,3)
    lineA$="So, "+charName$(gamChar)+", have you figured out"
    lineB$="how we should market this project yet?"
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=1 : gotim=0
  EndIf
  If negTopic=4
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,3)
    greenLineA$="Yes, '"+projTitle$(gamProject)+"' is the perfect choice!"
    greenLineB$="Now we can look ahead to the game's release..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  If negTopic=5
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,1)
    redLineA$="No, I'm not sure that's what we're looking for."
    redLineB$="I suggest you keep working on the project..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  If negTopic=6
   If negTim>25 And negTim<325 And gamDesk(camFoc)=-1
    Speak(camFoc,0,1)
    lineA$="I wish I could, but this damn computer is broken!"
    lineB$="When are you going to pay someone to fix it?"
   EndIf
   If negTim>25 And negTim<325 And gamDesk(camFoc)=-2
    Speak(camFoc,0,1)
    lineA$="This computer is still in the process of being repaired!"
    lineB$="I won't be able to get any work done until next week..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=1 : gotim=0
  EndIf
  If negTopic=7
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,1)
    lineA$="You dragged me down here to talk about releasing"
    lineB$="THIS?! Keep working on it until it takes shape..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  If negTopic=8
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,1)
    lineA$="We've already talked about this enough for one day!"
    lineB$="You should concentrate on finishing the project..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  If negTopic=9
   If negTim>25 And negTim<325
    Speak(camFoc,0,1)
    lineA$="I consider myself to be a "+textRole$(charDedicated(gamWorker))+" nowadays!"
    lineB$="Find somebody else to do the "+textAttribute$(gamDevelop)+"..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=1 : gotim=0
  EndIf
  If negTopic=10
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,1)
    lineA$=fedName$(fed)+" has a policy of only releasing games"
    lineB$="of the highest quality! This doesn't qualify yet..."
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  If negTopic=11
   If negTim>25 And negTim<325
    Speak(negVisitor,negStar,1)
    lineA$="I told you that we can't release this game"
    lineB$="until you get the "+textAttribute$(gamMission)+" up to "+gamTarget+"%!"
   EndIf
   If negTim>325 Then camFoc=negStar : screenState=2 : gotim=0
  EndIf
  DisplaySubtitles()
 EndIf
 ;cursor
 cursor=1
 If screenAgenda=0 Or screenAgenda=4 Or screenAgenda=8 Then cursor=0
 If screenAgenda=2 And screenState=2 Then cursor=0
 If cursor>0 Then DrawImage gCursor,MouseX(),MouseY()
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 And gotim>0 Then PlaySound sMenuSelect
 EndIf
 ;mask shaky start
 If gotim<0 Then Loader("","")

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
;free entities
If go=-1 Or screenAgenda<>2 Then Loader("Please Wait","Leaving Office")
FreeEntity world
FreeEntity cam 
FreeEntity camPivot
FreeEntity dummy
FreeEntity light
FreeEntity lightPivot
;remove workstations
If go=-1 Or screenAgenda<>2 Then Loader("","")
For cyc=1 To no_desks
 If gamDesk(cyc)<>0 And desk(cyc)>0
  FreeEntity desk(cyc) : desk(cyc)=0
 EndIf
Next
;remove characters
If go=-1 Or screenAgenda<>2 Then Loader("","")
For cyc=1 To no_plays
 If pChar(cyc)>0
  FreeEntity p(cyc)
  For limb=1 To 50
   If pShadow(cyc,limb)>0
    FreeEntity pShadow(cyc,limb)
   EndIf
  Next
 EndIf
Next
;update game box
If screenAgenda=6
 For cyc=1 To 3
  FreeImage projPossibleBox(cyc)
 Next
 If projIdentity(gamProject)>0 And projIdentified(gamProject)>0
  Loader("","")
  LoadProjectBox(gamProject)
 EndIf
EndIf
;save progress
If screenAgenda<>2
 Loader("","")
 SaveProgress(slot)
EndIf
;proceed
screen=20
If go=1 And screenAgenda=2 And optAuto=0 Then screen=60+Rnd(1,no_subgames)
If screenAgenda=8 Then screen=14
;automatic scores
;If go=1 And screenAgenda=2 And optAuto>0
 ;Loader("Please Wait","Simulating Progress")
 ;SimulateProgress()
 ;screen=50 : screenAgenda=4
;EndIf
End Function

;-----------------------------------------------------------------
;////////////////////// RELATED FUNCTIONS ////////////////////////
;-----------------------------------------------------------------

;PREPARE STUDIO
Function PrepareStudio()
 ;rest textures
 ResetTextures()
 ;camera
 LoadCamera()
 ;load workstations
 LoadWorkstations()
 ;universal scenery
 PrepareScenery()
 ;lighting
 LoadLighting()
 ;add visitor
 If (screenAgenda=6 Or screenAgenda=8) And gamChar<>fedBooker(fed)
  negChar=fedBooker(fed)
  If charTeam(negChar)<>gamChar
   no_plays=no_plays+1
   pChar(no_plays)=negChar
   negVisitor=no_plays
  EndIf
  pDesk(negVisitor)=negStar
  cyc=negVisitor : v=negStar
  PositionEntity dummy,deskX#(v),deskY#(v),deskZ#(v)
  RotateEntity dummy,0,deskA#(v),0
  MoveEntity dummy,-20,0,-5
  pX#(cyc)=EntityX(dummy) : pY#(cyc)=meetY# : pZ#(cyc)=EntityZ(dummy) : pA#(cyc)=deskA#(v)+Rnd(290,340) : pAnim(cyc)=Rnd(20,23)
  If pX#(cyc)>wEast# Then pX#(cyc)=wEast#
  If pX#(cyc)<wWest# Then pX#(cyc)=wWest#
  If pZ#(cyc)>wNorth# Then pZ#(cyc)=wNorth#
  If pZ#(cyc)<wSouth# Then pZ#(cyc)=wSouth#
 EndIf
 ;load actors
 For cyc=1 To no_plays
  If pChar(cyc)>0
   LoadActor(cyc,pChar(cyc))
  EndIf
 Next
 ;starting angles
 If screenAgenda=0 Or screenAgenda=2 Or screenAgenda=6 Or screenAgenda=8
  PositionEntity dummy,deskX#(negStar),0,deskZ#(negStar)
  RotateEntity dummy,0,deskA#(negStar),0
  If screenAgenda=0 Then MoveEntity dummy,-100,0,-100
  If screenAgenda=2 Then MoveEntity dummy,100,0,-200
  If screenAgenda=6 Then MoveEntity dummy,100,0,-100
  If screenAgenda=8 Then MoveEntity dummy,100,0,40
  camX#=EntityX(dummy) : camY#=wCeiling# : camZ#=EntityZ(dummy)   
 EndIf
 ;handheld offset
 If screenAgenda=4 And hardHandheld(gamHardware)>0 And projIdentity(gamProject)>0
  PositionEntity dummy,camX#,camY#,camZ#
  RotateEntity dummy,0,deskA#(camFoc),0
  MoveEntity dummy,5,-5,0
  camX#=EntityX(dummy) : camY#=EntityY(dummy) : camZ#=EntityZ(dummy)   
 EndIf
 ;restore textures
 RestoreTextures()
End Function

;LOAD WORKSTATIONS
Function LoadWorkstations()
 ;empty office
 Loader("","")
 If gamOffice=1
  world=LoadAnimMesh("World/Office/Empty Office.3ds")
  DecorateOffice(fed)
  ;dimensions
  wNorth#=110 : wSouth#=-110 : wWest#=-100 : wEast#=105
  wGround#=5 : wCeiling#=75
  lightX#=0 : lightY#=wGround#+60 : lightZ#=0 
  meetX#=Rnd(-50,50) : meetY#=wGround# : meetZ#=Rnd(-50,50) : meetA#=Rnd(0,360)
  If screenAgenda<>4
   randy=Rnd(0,2)
   If randy=0 Then camX#=0
   If randy=1 Then camX#=wWest#
   If randy=2 Then camX#=wEast#
   randy=Rnd(0,2)
   If randy=0 Then camZ#=0
   If randy=1 Then camZ#=wSouth#
   If randy=2 Then camZ#=wNorth#
   camY#=Rnd(wGround#,wCeiling#)
  EndIf
  ;desk positions
  no_desks=6 : rotor=gamDeskPosition
  For n=1 To no_desks
   If rotor=1 Then deskX#(n)=75 : deskY#(n)=wGround# : deskZ#(n)=23 : deskA#(n)=270
   If rotor=2 Then deskX#(n)=89 : deskY#(n)=wGround# : deskZ#(n)=-79 : deskA#(n)=180
   If rotor=3 Then deskX#(n)=-11 : deskY#(n)=wGround# : deskZ#(n)=-79 : deskA#(n)=180
   If rotor=4 Then deskX#(n)=-73 : deskY#(n)=wGround# : deskZ#(n)=9 : deskA#(n)=90
   If rotor=5 Then deskX#(n)=-73 : deskY#(n)=wGround# : deskZ#(n)=81 : deskA#(n)=90
   If rotor=6 Then deskX#(n)=63 : deskY#(n)=wGround# : deskZ#(n)=82 : deskA#(n)=0
   rotor=rotor+1
   If rotor>no_desks Then rotor=1
  Next
 EndIf
 ;add workstations
 Loader("","")
 For cyc=1 To no_desks
  desk(cyc)=0 : excluded=0
  If screenAgenda<>2 And screenAgenda<>4 And charRoster(gamChar,cyc)<>gamChar And charRoster(gamChar,cyc)<>negChar And optEntourage=<1
   randy=Rnd(0,4)
   If randy=<1 Or optEntourage=0 Then excluded=1
  EndIf
  If gamDesk(cyc)<>0 And excluded=0
   desk(cyc)=LoadAnimMesh("World/Workstations/Workstation.3ds")
   PositionEntity desk(cyc),deskX#(cyc),deskY#(cyc),deskZ#(cyc)
   RotateEntity desk(cyc),0,deskA#(cyc),0
   Loader("","")
   PrepareHardware(cyc)
  EndIf
 Next
 ;prepare cast
 negStar=0
 no_plays=optTeamSize
 For cyc=1 To optPlayLim
  pChar(cyc)=0 : pDesk(cyc)=0
 Next
 For cyc=1 To optTeamSize
  char=charRoster(gamChar,cyc) : excluded=0
  If charVacant(char)>0 Or gamDesk(cyc)=0 Or desk(cyc)=0 Then excluded=1
  If screenAgenda<>2 And screenAgenda<>4 And char<>gamChar And char<>negChar And optEntourage=<1
   randy=Rnd(0,4)
   If randy=<1 Or optEntourage=0 Then excluded=1
  EndIf
  If char>0 And excluded=0 And cyc=<no_desks
   pChar(cyc)=char : pDesk(cyc)=cyc
   If pChar(cyc)=gamChar Then negStar=cyc
   If screen=53 And char=negChar Then negVisitor=cyc
   If screenAgenda=6 And char=fedBooker(fed) Then negVisitor=cyc
   pX#(cyc)=deskX#(cyc) : pY#(cyc)=deskY#(cyc) : pZ#(cyc)=deskZ#(cyc)
   pA#(cyc)=deskA#(cyc)
   pAnim(cyc)=Rnd(1,20)
   If pAnim(cyc)>10 Then pAnim(cyc)=10 
  EndIf
 Next
 ;huddle positions
 If screen=50 And (screenAgenda=6 Or screenAgenda=8)
  spot=0
  For cyc=1 To no_plays 
   If cyc<>negStar
    PositionEntity dummy,deskX#(negStar),deskY#(negStar),deskZ#(negStar)
    RotateEntity dummy,0,deskA#(negStar),0
    spot=spot+1
    If spot=1 Then MoveEntity dummy,20,0,-5
    If spot=2 Then MoveEntity dummy,10,0,-15
    If spot=3 Then MoveEntity dummy,-10,0,-15
    If spot=4 Then MoveEntity dummy,0,0,-25
    If spot=5 Then MoveEntity dummy,15,0,-25
    If spot=6 Then MoveEntity dummy,-15,0,-25
    If spot=7 Then MoveEntity dummy,30,0,-10
    If spot=8 Then MoveEntity dummy,-30,0,-10
    randy=Rnd(0,1)
    If randy=0 And spot=<8
     pX#(cyc)=EntityX(dummy) : pY#(cyc)=meetY# : pZ#(cyc)=EntityZ(dummy) : pAnim(cyc)=Rnd(20,23)
     PointEntity dummy,FindChild(desk(negStar),"Monitor")
     pA#(cyc)=EntityYaw(dummy,1)
    EndIf
   EndIf
  Next
 EndIf
End Function

;PREPARE HARDWARE ON DESK
Function PrepareHardware(cyc)
 ;identify hardware
 deskHardware(cyc)=gamHardware
 If screenAgenda=8 Then deskHardware(cyc)=gamNewHardware
 ;hide by default
 HideEntity FindChild(desk(cyc),"Keyboard")
 HideEntity FindChild(desk(cyc),"Monitor")
 HideEntity FindChild(desk(cyc),"MacTV") 
 HideEntity FindChild(desk(cyc),"OldTV")
 HideEntity FindChild(desk(cyc),"BoxTV")
 HideEntity FindChild(desk(cyc),"BigTV")
 HideEntity FindChild(desk(cyc),"Laptop") 
 If GetYear(gamDate)>2000 Then hardModel$(18)="Laptop" : hardCom$(18)="Laptop" : hardTV$(18)="Laptop"
 HideEntity FindChild(desk(cyc),"Spectrum") : HideEntity FindChild(desk(cyc),"Speccy")
 If hardReleased(4)>0 And gamDate>hardReleased(4)+4
  hardModel$(2)="Speccy" : hardCom$(2)="Speccy" 
 Else 
  hardModel$(2)="Spectrum" : hardCom$(2)="Spectrum"
 EndIf
 For v=1 To no_machines
  If FindChild(desk(cyc),hardModel$(v))>0
   HideEntity FindChild(desk(cyc),hardModel$(v))
  EndIf
 Next
 If hardModel$(deskHardware(cyc))=hardCom$(deskHardware(cyc)) Then HideEntity FindChild(desk(cyc),"Cable03")
If gamDesk(cyc)<>0
 ;screenshot texture
 If projIdentity(gamProject)=>0
  tScreenshot=LoadTexture("Games/Screenshots/Screen"+Dig$(projIdentity(gamProject),100)+".JPG")
 Else
  tScreenshot=tDevelop(Abs(projIdentity(gamProject)))
 EndIf
 ;show computer
 ShowEntity FindChild(desk(cyc),hardCom$(deskHardware(cyc)))
 If FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S")>0
  EntityTexture FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S"),tScreenshot
  EntityFX FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S"),9 
  If HideScreen(cyc)
   HideEntity FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S")
  EndIf
 EndIf
 ;hide superfluous laptop
 If screen=50 And (screenAgenda=0 Or screenAgenda=6 Or screenAgenda=8)
  If hardCom$(deskHardware(cyc))="Laptop" And hardCom$(deskHardware(cyc))<>hardTV$(deskHardware(cyc)) And hardCom$(deskHardware(cyc))<>hardModel$(deskHardware(cyc))
   HideEntity FindChild(desk(cyc),hardCom$(deskHardware(cyc)))
   HideEntity FindChild(desk(cyc),"Cable02")
   HideEntity FindChild(desk(cyc),"Cable01")
   HideEntity FindChild(desk(cyc),"Mouse")
  EndIf
 EndIf
 ;show monitor/TV
 ShowEntity FindChild(desk(cyc),hardTV$(deskHardware(cyc)))
 EntityTexture FindChild(desk(cyc),hardTV$(deskHardware(cyc))+"S"),tScreenshot
 EntityFX FindChild(desk(cyc),hardTV$(deskHardware(cyc))+"S"),9
 If HideScreen(cyc)
  HideEntity FindChild(desk(cyc),hardTV$(deskHardware(cyc))+"S")
 EndIf
 ;show console
 If deskHardware(cyc)=33 Then ShowEntity FindChild(desk(cyc),"Wii")
 If FindChild(desk(cyc),hardModel$(deskHardware(cyc)))>0
  ShowEntity FindChild(desk(cyc),hardModel$(deskHardware(cyc)))
  If screenAgenda=8 And hardHandheld(gamNewHardware)>0 Then HideEntity FindChild(desk(cyc),hardModel$(deskHardware(cyc)))
  If FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S")>0
   EntityTexture FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S"),tScreenshot
   EntityFX FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S"),9 
   If HideScreen(cyc) Or hardModel$(deskHardware(cyc))<>hardCom$(deskHardware(cyc))
    HideEntity FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S")
   EndIf
  EndIf
 EndIf
 If projIdentity(gamProject)=>0 Then FreeTexture tScreenshot
EndIf
 ;boxes on desk
 For v=1 To 2
  If gamDesk(cyc)=0
   HideEntity FindChild(desk(cyc),"Box"+Dig$(v,10))
  Else
   If deskGame(cyc,v)=0 Then deskGame(cyc,v)=chartGame(Rnd(2,20))
   tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(deskGame(cyc,v),100)+".JPG")
   EntityTexture FindChild(desk(cyc),"Box"+Dig$(v,10)),tBoxArt
   FreeTexture tBoxArt
  EndIf
 Next
 ;desk items
 EntityTexture FindChild(desk(cyc),"Plant"),tPlant(deskPlant(cyc))
 randy=Rnd(0,1)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Plant") : HideEntity FindChild(desk(cyc),"Pot")
 randy=Rnd(0,2)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Paper01")
 randy=Rnd(0,2)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Paper02")
 randy=Rnd(0,2)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Phone")
 randy=Rnd(0,2)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Book")
 randy=Rnd(0,2)
 If randy=0 Then HideEntity FindChild(desk(cyc),"Box02")
End Function

;HIDE SCREEN?
Function HideScreen(cyc)
 value=0
 If charRoster(gamChar,cyc)=0 Or charVacant(charRoster(gamChar,cyc))>0 Then value=1
 If gamDesk(cyc)<0
  If (screen=50 And screenAgenda>0 And screenAgenda<>6) Or screen<>50 Then value=1
 EndIf
 If screenAgenda=8 Then value=1
 Return value
End Function

;TEXTURE SCREENS
Function TextureScreens(cyc,tex)
 EntityTexture FindChild(desk(cyc),hardTV$(deskHardware(cyc))+"S"),tex ;monitor
 If FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S")>0
  EntityTexture FindChild(desk(cyc),hardCom$(deskHardware(cyc))+"S"),tex ;laptop
 EndIf
 If FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S")>0
  EntityTexture FindChild(desk(cyc),hardModel$(deskHardware(cyc))+"S"),tex ;handheld
 EndIf
End Function

;POINT HEAD
Function PointHead(cyc,entity)
 ;identify limbs involved
 limb=FindChild(p(cyc),"Head")
 source=FindChild(p(cyc),"Body")
 ;stabilize and point
 RotateEntity limb,EntityPitch(source),EntityYaw(source),EntityRoll(source) 
 PointEntity limb,entity
 ;X limitations
 If EntityPitch(limb)<EntityPitch(source)-60 Then RotateEntity limb,EntityPitch(source)-60,EntityYaw(limb),EntityRoll(limb)
 If EntityPitch(limb)>EntityPitch(source)+10 Then RotateEntity limb,EntityPitch(source)+10,EntityYaw(limb),EntityRoll(limb)
 ;Y limitations
 If EntityYaw(limb)<EntityYaw(source)-40 Then RotateEntity limb,EntityPitch(limb),EntityYaw(source)-40,EntityRoll(limb)
 If EntityYaw(limb)>EntityYaw(source)+40 Then RotateEntity limb,EntityPitch(limb),EntityYaw(source)+40,EntityRoll(limb) 
 ;Z limitations
 RotateEntity limb,EntityPitch(limb),EntityYaw(limb),0
 ;preserve long hair
 hair=FindChild(p(cyc),"H_Long")
 pitch=-EntityPitch(limb)
 If pitch<10 Then pitch=pitch+(GetDiff#(pitch,10)/4)
 RotateEntity hair,pitch,-(EntityYaw(limb)/2),0
End Function

;SIMULATE PROGRESS
Function SimulateProgress()
 GetGameSettings()
 gamLimit=gamLimit/2 : gamMaxScore=gamLimit
 If gamDevelop=>1 And gamDevelop=<6
  gamScore(gamDevelop)=Rnd(0,gamMaxScore)
  gamScore(0)=gamScore(gamDevelop)
 Else
  For count=0 To 6
   gamScore(count)=0
  Next
  gamScore(0)=Rnd(0,gamMaxScore)
  While gamScore(0)>0
   randy=Rnd(1,6)
   gamScore(randy)=gamScore(randy)+1
   gamScore(0)=gamScore(0)-1
  Wend
  gamScore(0)=gamScore(1)+gamScore(2)+gamScore(3)+gamScore(4)+gamScore(5)+gamScore(6)
 EndIf
 screen=60 : UpdateProject(gamProject)
End Function

;UPDATE PROJECT
Function UpdateProject(cyc)
 ;reset stats
 gamIncrease=0
 projOldGameplay(cyc)=projGameplay(cyc)
 projOldReplay(cyc)=projReplay(cyc)
 projOld2D(cyc)=proj2D(cyc)
 projOld3D(cyc)=proj3D(cyc)
 projOldSound(cyc)=projSound(cyc)
 projOldMusic(cyc)=projMusic(cyc)
 projNewGameplay(cyc)=projGameplay(cyc)
 projNewReplay(cyc)=projReplay(cyc)
 projNew2D(cyc)=proj2D(cyc)
 projNew3D(cyc)=proj3D(cyc)
 projNewSound(cyc)=projSound(cyc)
 projNewMusic(cyc)=projMusic(cyc)
 ;improve stats 
 char=gamWorker
 divider=1
 If screen=61 Then divider=5
 If screen=62 Or screen=63 Or screen=65 Then divider=2
 perfecto=PercentOf#(gamMaxScore/divider,90) : gamPerfect=0
 If gamDevelop=1
  If projGameplay(cyc)=>hardGameplay(projFormat(cyc))-perfecto Then projNewGameplay(cyc)=hardGameplay(projFormat(cyc))-perfecto : gamPerfect=1
  projNewGameplay(cyc)=projNewGameplay(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNewGameplay(cyc)>hardGameplay(projFormat(cyc)) Then projNewGameplay(cyc)=hardGameplay(projFormat(cyc))
  gamIncrease=projNewGameplay(cyc)-projGameplay(cyc)
  charDesign(char)=charDesign(char)+PursueValue(charDesign(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 If gamDevelop=2
  If projReplay(cyc)=>hardReplay(projFormat(cyc))-perfecto Then projNewReplay(cyc)=hardReplay(projFormat(cyc))-perfecto : gamPerfect=1
  projNewReplay(cyc)=projNewReplay(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNewReplay(cyc)>hardReplay(projFormat(cyc)) Then projNewReplay(cyc)=hardReplay(projFormat(cyc))
  gamIncrease=projNewReplay(cyc)-projReplay(cyc)
  charProgram(char)=charProgram(char)+PursueValue(charProgram(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 If gamDevelop=3
  If proj2D(cyc)=>hard2D(projFormat(cyc))-perfecto Then projNew2D(cyc)=hard2D(projFormat(cyc))-perfecto : gamPerfect=1
  projNew2D(cyc)=projNew2D(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNew2D(cyc)>hard2D(projFormat(cyc)) Then projNew2D(cyc)=hard2D(projFormat(cyc))
  gamIncrease=projNew2D(cyc)-proj2D(cyc)
  char2D(char)=char2D(char)+PursueValue(char2D(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 If gamDevelop=4
  If proj3D(cyc)=>hard3D(projFormat(cyc))-perfecto Then projNew3D(cyc)=hard3D(projFormat(cyc))-perfecto : gamPerfect=1
  projNew3D(cyc)=projNew3D(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNew3D(cyc)>hard3D(projFormat(cyc)) Then projNew3D(cyc)=hard3D(projFormat(cyc))
  gamIncrease=projNew3D(cyc)-proj3D(cyc)
  char3D(char)=char3D(char)+PursueValue(char3D(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 If gamDevelop=5
  If projSound(cyc)=>hardSound(projFormat(cyc))-perfecto Then projNewSound(cyc)=hardSound(projFormat(cyc))-perfecto : gamPerfect=1
  projNewSound(cyc)=projNewSound(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNewSound(cyc)>hardSound(projFormat(cyc)) Then projNewSound(cyc)=hardSound(projFormat(cyc))
  gamIncrease=projNewSound(cyc)-projSound(cyc)
  charSound(char)=charSound(char)+PursueValue(charSound(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 If gamDevelop=6
  If projMusic(cyc)=>hardMusic(projFormat(cyc))-perfecto Then projNewMusic(cyc)=hardMusic(projFormat(cyc))-perfecto : gamPerfect=1
  projNewMusic(cyc)=projNewMusic(cyc)+PercentOf#(gamScore(gamDevelop)/divider,gamSkill)
  If projNewMusic(cyc)>hardMusic(projFormat(cyc)) Then projNewMusic(cyc)=hardMusic(projFormat(cyc))
  gamIncrease=projNewMusic(cyc)-projMusic(cyc)
  charMusic(char)=charMusic(char)+PursueValue(charMusic(char),GetPercent#(gamScore(gamDevelop),gamMaxScore),1)
 EndIf
 ;holistic changes
 If gamDevelop=7
  projNewGameplay(cyc)=projNewGameplay(cyc)+PercentOf#(gamScore(1)/divider,charDesign(char))
  If gamScore(1)>0 And projNewGameplay(cyc)=projGameplay(cyc) Then projNewGameplay(cyc)=projGameplay(cyc)+1
  If projNewGameplay(cyc)>hardGameplay(projFormat(cyc)) Then projNewGameplay(cyc)=hardGameplay(projFormat(cyc))
  gamIncrease=gamIncrease+(projNewGameplay(cyc)-projGameplay(cyc))
  projNewReplay(cyc)=projNewReplay(cyc)+PercentOf#(gamScore(2)/divider,charProgram(char))
  If gamScore(2)>0 And projNewReplay(cyc)=projReplay(cyc) Then projNewReplay(cyc)=projReplay(cyc)+1
  If projNewReplay(cyc)>hardReplay(projFormat(cyc)) Then projNewReplay(cyc)=hardReplay(projFormat(cyc))
  gamIncrease=gamIncrease+(projNewReplay(cyc)-projReplay(cyc))
  projNew2D(cyc)=projNew2D(cyc)+PercentOf#(gamScore(3)/divider,char2D(char))
  If gamScore(3)>0 And projNew2D(cyc)=proj2D(cyc) Then projNew2D(cyc)=proj2D(cyc)+1
  If projNew2D(cyc)>hard2D(projFormat(cyc)) Then projNew2D(cyc)=hard2D(projFormat(cyc))
  gamIncrease=gamIncrease+(projNew2D(cyc)-proj2D(cyc))
  projNew3D(cyc)=projNew3D(cyc)+PercentOf#(gamScore(4)/divider,char3D(char))
  If gamScore(4)>0 And projNew3D(cyc)=proj3D(cyc) Then projNew3D(cyc)=proj3D(cyc)+1
  If projNew3D(cyc)>hard3D(projFormat(cyc)) Then projNew3D(cyc)=hard3D(projFormat(cyc))
  gamIncrease=gamIncrease+(projNew3D(cyc)-proj3D(cyc))
  projNewSound(cyc)=projNewSound(cyc)+PercentOf#(gamScore(5)/divider,charSound(char))
  If gamScore(5)>0 And projNewSound(cyc)=projSound(cyc) Then projNewSound(cyc)=projSound(cyc)+1
  If projNewSound(cyc)>hardSound(projFormat(cyc)) Then projNewSound(cyc)=hardSound(projFormat(cyc))
  gamIncrease=gamIncrease+(projNewSound(cyc)-projSound(cyc))
  projNewMusic(cyc)=projNewMusic(cyc)+PercentOf#(gamScore(6)/divider,charMusic(char))
  If gamScore(6)>0 And projNewMusic(cyc)=projMusic(cyc) Then projNewMusic(cyc)=projMusic(cyc)+1
  If projNewMusic(cyc)>hardMusic(projFormat(cyc)) Then projNewMusic(cyc)=hardMusic(projFormat(cyc))
  gamIncrease=gamIncrease+(projNewMusic(cyc)-projMusic(cyc))
 EndIf
 ;update identity
 projOldIdentity(cyc)=projIdentity(cyc)
 projIdentity(cyc)=IdentifyProject(cyc)
 If gamIncrease=0 Then projIdentity(cyc)=projOldIdentity(cyc)
 If projIdentity(cyc)=<0
  projIdentity(cyc)=projOldIdentity(cyc)
  If gamDevelop=>1 And gamDevelop=<6 And projOldIdentity(cyc)=<0 Then projIdentity(cyc)=-gamDevelop
 EndIf
 ;update screenshot
 If projIdentity(cyc)<>projOldIdentity(cyc)
  If optAuto=0 Then Loader("","")
  LoadProjectScreen(cyc)
  If projIdentity(cyc)>0 And projIdentified(cyc)>0
   LoadProjectBox(cyc)
  EndIf
 EndIf
 ;credit worker
 gamNewCredit=0
 charWorked(char)=charWorked(char)+1
 If gamScore(0)>gamHiScore(screen-60) Then gamHiScore(screen-60)=gamScore(0) : gamHiCredit(screen-60)=char
 If gamDevelop=>1 And gamDevelop=<6 And gamIncrease>projHiIncrease(cyc,gamDevelop)
  projHiIncrease(cyc,gamDevelop)=gamIncrease
  projCredit(cyc,gamDevelop)=char
  gamNewCredit=gamDevelop
 EndIf
 If char<>gamChar And projCredit(cyc,gamDevelop)=char And projIdentity(cyc)>0 And gamMission<>gamDevelop ;And gamAgreement(1)=0
  If gamPerfect>0 Then randy=Rnd(0,5) Else randy=Rnd(0,10)
  If randy=<1 
   If gamDevelop=1 And projGameplay(gamProject)>hardGameplay(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
   If gamDevelop=2 And projReplay(gamProject)>hardGameplay(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
   If gamDevelop=3 And proj2D(gamProject)>hard2D(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
   If gamDevelop=4 And proj3D(gamProject)>hard3D(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
   If gamDevelop=5 And projSound(gamProject)>hardSound(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
   If gamDevelop=6 And projMusic(gamProject)>hardMusic(projFormat(gamProject))/2 Then projLocked(cyc,gamDevelop)=char
  EndIf
 EndIf
End Function