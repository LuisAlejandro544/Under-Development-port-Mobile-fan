;//////////////////////////////////////////////////////////////////////////////
;--------------------------- UNDER DEVELOPMENT: CREDITS -----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;--------------------------------- INTRO --------------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Intro()
;prepare images
LoadFonts()
gBackground=LoadImage("Graphics/Background.png")
If GraphicsWidth()<>800 And GraphicsHeight()<>600 Then ResizeImage gBackground,GraphicsWidth(),GraphicsHeight()
MaskImage gBackground,255,0,255
gLogo(1)=LoadImage("Graphics/Logo01.png")
MaskImage gLogo(1),255,0,255
gMDickie=LoadImage("Graphics/MDickie.png")
MaskImage gMDickie,10,10,10
For count=1 To 6
 gMenu(count)=LoadImage("Graphics/Menus/Menu0"+count+".png")
 MaskImage gMenu(count),255,0,255
Next
;foliage
For count=1 To no_plants
 tPlant(count)=LoadTexture("World/Foliage/Flower"+Dig$(count,10)+".png",4)
Next
;camera
cam=CreateCamera()
CameraViewport cam,0,0,GraphicsWidth(),GraphicsHeight()
CameraZoom cam,1.5 
PositionEntity cam,camX#,camY#,camZ# 
camType=10 : camOption=1
dummy=CreatePivot()
camPivot=CreatePivot()
camX#=0 : camY#=32.5 : camZ#=0
camA#=45 : camAccel=0 : angleAccel=100
;lighting
AmbientLight 220,210,200
no_lights=1
light=CreateLight(1) 
LightColor light,250,230,210
PositionEntity light,lightX#,lightY#,lightZ#
lightPivot=CreatePivot()
PositionEntity lightPivot,lightX#+Rnd(-100,100),lightY#,lightZ#+Rnd(-110,110) 
PointEntity light,lightPivot
arenaLight=1 : arenaAtmos=0
ambR#=0 : ambG#=0 : ambB#=0
lightR#=0 : lightG#=0 : lightB#=0
;load scene
world=LoadAnimMesh("World/Office/Office.3ds")
EntityAlpha FindChild(world,"Window"),0.5
PrepareScenery()
;apply posters
tempLogo=LoadTexture("Graphics/MDickie.png")
EntityTexture FindChild(world,"Logo"),tempLogo
FreeTexture tempLogo
For v=1 To 3
   If FindChild(world,"Poster"+Dig$(v,10))>0
    randy=Rnd(281,305)
    tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(randy,100)+".JPG")
    EntityTexture FindChild(world,"Poster"+Dig$(v,10)),tBoxArt
    FreeTexture tBoxArt
   EndIf
Next
;load actor
cyc=1 : pChar(cyc)=0
charBaggy(pChar(cyc))=3
p(cyc)=LoadAnimMesh("World/Workstations/Normal.3ds")
pSeq(cyc,604)=LoadAnimSeq(p(cyc),"Characters/Animation/Gestures.3ds")
pSeq(cyc,1)=ExtractAnimSeq(p(cyc),2770,2850,pSeq(cyc,604)) ;typing
pSeq(cyc,2)=ExtractAnimSeq(p(cyc),2720,2760,pSeq(cyc,604)) ;laptop turn
Animate p(cyc),1,1.0,pSeq(cyc,1),0
PositionEntity p(cyc),0,5,67
RotateEntity p(cyc),0,180,0
scaler#=0.06
ScaleEntity p(cyc),scaler#,scaler#,scaler#
;dress actor
GetLimbs(cyc)
RemoveHair(cyc)
RemoveHeadwear(cyc)
RemoveEyewear(cyc)
RemoveAccessories(cyc)
ApplyHandhelds(cyc)
tHat(0)=LoadTexture("Characters/Headwear/Hat09.JPG")
ShowEntity FindChild(p(cyc),"Hat")
EntityTexture FindChild(p(cyc),"Hat"),tHat(0),0,1
PositionEntity FindChild(p(cyc),"Hat"),2.43,41.62,5.14 
RotateEntity FindChild(p(cyc),"Hat"),20,-18.9,-7.4
tHair(0)=LoadTexture("Characters/Hair/Hair03.JPG")
EntityAlpha FindChild(p(cyc),"H_Bald"),1
EntityTexture FindChild(p(cyc),"H_Bald"),tHair(0);,0,1
tFace(0)=LoadTexture("Characters/Faces/Face06.JPG")
tEars=LoadTexture("Characters/Expressions/Ears.JPG")
tEyes(0)=LoadTexture("Characters/Expressions/Eyes02.JPG")
EntityTexture pLimb(cyc,1),tEyes(0),0,2
For limb=1 To 4
 EntityTexture pLimb(cyc,limb),tFace(0),0,1
 If limb=>2 And limb=<3 Then EntityTexture pLimb(cyc,limb),tEars,0,3
Next
tBody(0)=LoadTexture("Characters/Bodies/Body31.JPG")
EntityTexture pLimb(cyc,5),tBody(0),0,1
EntityAlpha FindChild(p(cyc),"Body"),0
tArm(0)=LoadTexture("Characters/Arms/Arm08 (5).JPG")
tHands(0)=LoadTexture("Characters/Hands/Hands01 (1).JPG")
For limb=6 To 31
 If (limb=>8 And limb=<18) Or (limb=>21 And limb=<31)
  EntityTexture pLimb(cyc,limb),tHands(0),0,1
 Else
  EntityTexture pLimb(cyc,limb),tArm(0),0,1
 EndIf
Next
tLegs(0)=LoadTexture("Characters/Legs/Legs52.JPG")
For limb=36 To 44
 EntityTexture pLimb(cyc,limb),tLegs(0),0,1
Next
EntityAlpha FindChild(p(cyc),"L_Leg"),0
EntityAlpha FindChild(p(cyc),"R_Leg"),0
EntityAlpha FindChild(p(cyc),"L_Short"),0
EntityAlpha FindChild(p(cyc),"R_Short"),0
;typing loop
sTheme(0)=LoadSound("Music/Theme00.mp3")
sTypeLoop=LoadSound("Sound/Typing.wav")
LoopSound sTypeLoop
chTyping=PlaySound(sTypeLoop)
ChannelVolume chTyping,0.5
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
musicTrigger=0 : animTrigger=0
camTrigger=0 : crowdTrigger=0
leaveTim=0 : quitTim=0
go=0 : gotim=-25
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL
    gotim=gotim+1
	If gotim>50 And keytim=0
	 If KeyDown(1) Or KeyDown(28) Or ButtonPressed(1) Or MouseDown(1) Then quitTim=1 : PlaySound sMenuGo : keytim=10
	EndIf  
	If quitTim>0 Then quitTim=quitTim+1
	If quitTim>2 Then go=1
	;trigger theme
	;If gotim=107 And ChannelPlaying(chTheme)=0 Then PlayTheme(-1) : musicTrigger=gotim
    ;trigger gesture
    If gotim=385 ;Or KeyDown(57) 
     StopChannel chTyping
     Animate p(1),3,1.25,pSeq(1,2),5
     Animate world,3,1.0,0,10
     animTrigger=gotim
	EndIf
	;trigger travel
	If gotim>375 And camAccel=0 Then camAccel=1 : camTrigger=gotim 
	If camZ#=>40 
	 ;If crowdTrigger=0 Then ProduceSound(0,sCrowd(7),0,0.25) : crowdTrigger=1
	 ;ChannelVolume chCrowd,0.1
	EndIf
	If camZ#=>35 And ChannelPlaying(chTheme)=0
     LoopSound sTheme(0)
     chTheme=PlaySound(sTheme(0))
	EndIf 
	If camZ#=>40 And leaveTim=0 Then camZ#=40 : ProduceSound(0,sCheer,0,0.1) : leaveTim=1
	If leaveTim>0 Then leaveTim=leaveTim+1
	If leaveTim>2 Then go=1
	
	;CAMERA
	If go=0 And leaveTim=0
	 ;position
	 If camAccel>0
	  camAccel=camAccel+1
      If camAccel>100 Then camAccel=100
      camZ#=camZ#+PercentOf#(3,camAccel)
      camY#=camY#-PercentOf#(0.2,camAccel)
     EndIf
     If camZ#>40 Then camZ#=40
     If camY#<28 Then camY#=28
	 PositionEntity cam,camX#,camY#,camZ# 
	 ;orientation
	 If gotim>365 Then angleAccel=angleAccel-1
	 If angleAccel<0 Then angleAccel=0 
	 If gotim>0 Then camA#=camA#-PercentOf#(1.0,angleAccel)
	 If camA#<-360 Then camA#=-360
	 RotateEntity cam,0,camA#,0
     ;atmosphere
     ManageAtmos()
    EndIf
	
 If go=0 And leaveTim=0 Then UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 ;SetFont font(4)
 ;Outline("Gotim: "+gotim,100,25,0,0,0,255,255,255)
 ;Outline("animTrigger: "+animTrigger,100,50,0,0,0,255,255,255)
 If gotim<0 Then Color 0,0,0 : Rect 0,0,GraphicsWidth(),GraphicsHeight()
 ;If quitTim>0 Then DrawImage gBackground,rX#(400),rY#(300)

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed
If quitTim>0 Then screen=1
Loader("Please Wait","Loading Game")
;ChannelVolume chCrowd,0.05
;If quitTim>0 Then StopChannel chCrowd
If ChannelPlaying(chTyping) Then StopChannel chTyping
FreeTimer timer
;start music
If ChannelPlaying(chTheme)=0
 LoopSound sTheme(0)
 chTheme=PlaySound(sTheme(0))
EndIf
chCurrentTheme=0 : chThemeChange=0
chThemeVol#=1.0
;remove entities
FreeEntity cam
FreeEntity camPivot
FreeEntity dummy
FreeEntity light
FreeEntity lightPivot
FreeEntity world
;remove actor
FreeEntity p(1)
FreeTexture tHat(0)
FreeTexture tHair(0)
FreeTexture tFace(0)
FreeTexture tEars
FreeTexture tEyes(0)
FreeTexture tBody(0)
FreeTexture tArm(0)
FreeTexture tHands(0)
FreeTexture tLegs(0)
;remove arena textures
;For count=1 To 6
 ;FreeTexture tPoster(count)
;Next
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 1. MAIN MENU ----------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function MainMenu()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
editChar=0
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
	If gotim>35 And keytim=0
	 ;leave
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If MouseDown(1)
	  If foc=>1 And foc=<6 Then go=1
	  If foc=10 Then go=-1
	 EndIf
	EndIf
	;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawTitleScreen()
 ;options
 foc=0
 y=270
 If gotim>3 Then DrawOption(1,rX#(400),rY#(y),"CAREER","") : y=y+40
 If gotim>6 Then DrawOption(2,rX#(400),rY#(y),"ARCADE","") : y=y+45 
 If gotim>9 Then DrawOption(3,rX#(400),rY#(y),"EDIT CHARACTERS","") : y=y+40
 If gotim>12 Then DrawOption(4,rX#(400),rY#(y),"EDIT GAMES","") : y=y+45
 If gotim>15 Then DrawOption(5,rX#(400),rY#(y),"OPTIONS","") : y=y+40
 If gotim>18 Then DrawOption(6,rX#(400),rY#(y),"CREDITS","") : y=y+55
 If gotim>21 Then DrawOption(10,rX#(400),rY#(y),"<<< QUIT <<<","")  
 ;cursor
 If foc<>oldfoc
  If foc>0 Then PlaySound sMenuSelect 
  oldfoc=foc 
 EndIf
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go>0 Then PlaySound sMenuGo Else PlaySound sMenuBack 
If go=1 
 If foc=1 Then game=1 : screen=10 : screenAgenda=0
 If foc=2 Then game=0 : screen=16 : screenAgenda=0
 If foc=3 
  If slot<>0 Then Loader("Please Wait","Restoring Data")
  SwitchSlot(0)
  game=0 : screen=11 : screenAgenda=1
 EndIf
 If foc=4 Then editChar=no_games : editScroll=0 : screen=17
 If foc=5 Then screen=2
 If foc=6 Then screen=3
EndIf
If go=-1 Then screen=4
If go=2 Then game=0 : screen=10 : screenAgenda=1
End Function

;DRAW TITLE SCREEN
Function DrawTitleScreen()
 ;logos
 DrawImage gLogo(1),rX#(350),rY#(175)
 DrawImage gMDickie,rX#(655),rY#(525) 
 ;icons
 x=rX#(350)+80 : y=rY#(175)-50
 For count=1 To 6
  DrawImage gIconLarge(count),x,y
  x=x+50
 Next
 ;version
 SetFont font(2)
 OutlineStraight("v1.07",rX#(350)+285,rY#(175)+29,0,0,0,255,240,0)
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------------- 3. CREDITS -----------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Credits()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
ProduceSound(0,sCheer,0,0.5)
scroll=0
go=0 : gotim=0 : keytim=10
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
	 If KeyDown(1) Then go=1
	 If MouseDown(1) And (scroll<-2100 Or scroll>0) Then go=1 
	EndIf	
	;scroller
	If gotim>20
	 scroll=scroll-1
	 If KeyDown(200) Or MouseDown(1) Then scroll=scroll-5
	 If KeyDown(208) Or MouseDown(2) Then scroll=scroll+5
	 If scroll<-2100 Then scroll=600
	 If scroll>600 Then scroll=-2100
	EndIf
	;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;logo
 y=175
 DrawImage gLogo(1),rX#(350),rY#(y+scroll)
 x=rX#(350)+80
 For count=1 To 6
  DrawImage gIconLarge(count),x,rY#(y+scroll)-50
  x=x+50
 Next 
 SetFont font(10)
 Outline("Copyright © Mat Dickie 2011",rX#(350)+100,rY#(y+scroll)+80,0,0,0,255,250,0)
 SetFont fontNews(8)
 Outline("©",rX#(350)+60,rY#(y+scroll)+80,0,0,0,255,250,0)
 y=y+160
 DrawGameProfile(418,rX#(400),rY#(y+scroll),1) : y=y+165
 DrawCharProfile(5,rX#(470),rY#(y+scroll),1) : y=y+190
 ;messages
 SetFont font(12)
 Outline("At this juncture, I would normally",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("explain that this game was made",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("single-handedly in a couple of",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("months - but this project is",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("about everyone EXCEPT me!",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+60

 Outline("For a start, I should like to",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("thank the diligent curators",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("of gaming history who kept",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("everything alive and online",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("for others to appreciate:",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+50
 
 Outline("www.CoverBrowser.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.VGBoxArt.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.AtariAge.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.LemonAmiga.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.Lemon64.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.CPCGameReviews.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.GameThemeSongs.com",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("www.Wikipedia.org",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 Outline("Dave's Old Computers",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+50
 
 Outline("Special thanks to the many game",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("developers more talented than I",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("who created each of the classic",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("memories fondly recalled here...",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+50

 Outline("Specifically, this game is dedicated",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("to the memory of Steve Jobs - one of its",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("inspirations whose life was 'completed'",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+30
 Outline("on the day the project was completed:",rX#(400),rY#(y+scroll),0,0,0,255,255,255) : y=y+40
 DrawCharProfile(84,rX#(400),rY#(y+scroll),1) : y=y+195
 
 SetFont font(12)
 Outline("THANKS FOR PLAYING!",rX#(400),rY#(y+scroll),0,0,0,Rnd(0,255),Rnd(0,255),Rnd(0,255)) : y=y+100
 DrawImage gMDickie,rX#(400),rY#(y+scroll)
 ;diagnostic
 ;SetFont font(2)
 ;Outline(scroll,rX#(50),rY#(50),0,0,0,255,255,255)
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go=-1 Then PlaySound sMenuBack Else PlaySound sMenuGo
FreeTimer timer
screen=1
End Function

;//////////////////////////////////////////////////////////////////////////////
;-------------------------------- 4. OUTRO ------------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function Outro()
;reset theme
ChannelVolume chTheme,0.5
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
go=0 : gotim=0 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;PORTAL
    gotim=gotim+1
	If gotim>50
	 If KeyDown(1) Or MouseDown(1) Or gotim>500 Then go=1
	EndIf   
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawImage gOutro,rX#(505),rY#(354)
 DrawTitleScreen() 
 ;message
 SetFont font(10)
 x=315 : y=490
 Outline("This game belongs to the community",rX#(x),rY#(y),0,0,0,255,255,255) : y=y+25
 Outline("and is designed to be customized.",rX#(x),rY#(y),0,0,0,255,255,255) : y=y+25
 Outline("Check MDickie.com for all the latest",rX#(x),rY#(y),0,0,0,255,255,255) : y=y+25
 Outline("editing advice and free downloads!",rX#(x),rY#(y),0,0,0,255,255,255) : y=y+25

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
SaveOptions()
SaveUniverse()
SaveProgress(slot)
SaveWorld(slot)
SaveChars(slot)
StopChannel chTheme
If KeyDown(56) And KeyDown(45)
 LoopHole()
EndIf
screen=0
End Function