;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;---------------------------- 62. SPACE INVADERS ------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global no_invaders
Global spGunX#,spGunY#,spGroundY
Global spGunSpeed#,spEnemySpeed#,spBulletSpeed#
Dim spEnemy(100)
Dim spEnemyHP(100)
Dim spEnemyX#(100)
Dim spEnemyY#(100)
Dim spEnemyDir(100)
;bullets
Global no_bullets=1,bullCount
Dim bullX(100)
Dim bullY(100)
Dim bullA(100)
Dim bullSpeed(100)
Dim bullTim(100)
Dim bullState(100)
;bugs
Global no_bugs=10,bugCount
Dim bugX(10)
Dim bugY(10)
Dim bugTim(10)
Dim bugState(10)
Dim bugSpeed(10)
;explosions
Global no_explodes=40,exCount
Dim exX(50)
Dim exY(50)
Dim exTim(50)
Dim exState(50)

;GAME
Function SpaceInvaders()
;difficulty
GetGameSettings()
spGunSpeed#=1.0+(Float#(gamSkill)/15) : spBulletSpeed#=10+(Float#(gamSkill)/10)
gamMaxScore=gamLimit : no_invaders=gamLimit
spEnemySpeed#=2.0+(Float#(110-no_invaders)/100)
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;enemies
spGroundY=rY#(300)+220
x=rX#(400)-300 : y=rY#(300)-(gamLimit*2)
For cyc=1 To no_invaders
 If gamDevelop=>1 And gamDevelop=<6 Then spEnemy(cyc)=gamDevelop Else spEnemy(cyc)=Rnd(1,6)
 spEnemyHP(cyc)=1 
 randy=Rnd(0,10)
 If randy=<1 Then spEnemy(cyc)=8 : spEnemyHP(cyc)=Rnd(2,3)
 spEnemyX#(cyc)=x : spEnemyY#(cyc)=y : spEnemyDir(cyc)=1
 x=x+75
 If x>rX#(400)+300 Then x=rX#(400)-300 : y=y-75
Next
spGunX#=rX#(400) : spGunY#=spGroundY-25
;explosions
exCount=0
For v=1 To no_explodes
 exState(v)=0 : exTim(v)=0
Next
no_bullets=1 : bullCount=0
For v=1 To no_bullets
 bullState(v)=0 : bullTim(v)=0
Next
bugCount=0
For v=1 To no_bugs
 bugState(v)=0 : bugTim(v)=0
Next
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
screenState=0
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
	 ;quit
	 If KeyDown(1) And screenState=1
	  PlaySound sFail: keytim=20
	  screenState=-1 : gotim=0
	  CreateExplosion(spGunX#,spGunY#)  
	  For count=0 To 6
	   gamNewScore(count)=gamNewScore(count)/2
	  Next
	 EndIf
	 ;proceed
	 If KeyDown(1) Or MouseDown(1) Or DirectionPressed() Or ButtonPressed(1) Or (screenState<0 And gotim>200) Or (screenState=0 And gotim>300)
	  If screenState<0 And gamScore(0)=gamNewScore(0) And gotim>50 Then go=1
	  If screenState=0 Then screenState=1 : gotim=0 : keytim=20
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;INVADERS
	If screenState=1
	 ;input
	 If KeyDown(203) Or JoyXDir()<0 Then spGunX#=spGunX#-spGunSpeed#
	 If KeyDown(205) Or JoyXDir()>0 Then spGunX#=spGunX#+spGunSpeed#
	 If spGunX#<rX#(400)-350 Then spGunX#=rX#(400)-350
	 If spGunX#>rX#(400)+350 Then spGunX#=rX#(400)+350
	 If (ButtonPressed(1) Or KeyDown(200)) And bullState(1)=0
	  ProduceSound(0,sFire,22050,1)
	  FireBullet(spGunX#,spGunY#)
	 EndIf 
	 ;bullets
	 For cyc=1 To no_bullets
 	  If bullState(cyc)>0
	   bullY(cyc)=bullY(cyc)-spBulletSpeed#
	   For v=1 To no_invaders
	    If spEnemy(v)>0 And spEnemyY#(v)>0
	     If bullX(cyc)>spEnemyX#(v)-23 And bullX(cyc)<spEnemyX#(v)+23 And bullY(cyc)>spEnemyY#(v)-20 And bullY(cyc)<spEnemyY#(v)+20 
	      ProduceSound(0,sExplode,22050,0)
	      CreateExplosion(bullX(cyc),bullY(cyc))  
	      spEnemyHP(v)=spEnemyHP(v)-1 : bullState(cyc)=0
	      If spEnemyHP(v)=<0
	       ProduceSound(0,sCollect,0,0.5)
	       CreateExplosion(spEnemyX#(v),spEnemyY#(v)) 
	       ScorePoint(spEnemy(v))
	       spEnemy(v)=0
	      EndIf
	     EndIf
	    EndIf
	   Next
	   If bullY(cyc)<0 Then bullState(cyc)=0  
	  EndIf
	 Next
	 ;invaders
	 drop=0
	 For cyc=1 To no_invaders
	  If spEnemy(cyc)>0
 	   If spEnemyDir(cyc)=1 Then spEnemyX#(cyc)=spEnemyX#(cyc)+spEnemySpeed# Else spEnemyX#(cyc)=spEnemyX#(cyc)-spEnemySpeed#
 	   If spEnemyX#(cyc)<rX#(400)-350 Then drop=-1
       If spEnemyX#(cyc)>rX#(400)+350 Then drop=1
       If spEnemyY#(cyc)>spGroundY-20 Or (spEnemyX#(cyc)>spGunX#-30 And spEnemyX#(cyc)<spGunX#+30 And spEnemyY#(cyc)>spGunY#-45 And spEnemyY#(cyc)<spGunY#+45) 
	    PlaySound sExplode
	    CreateExplosion(spGunX#,spGunY#) 
	    PlaySound sFail : screenState=-1 : gotim=0
	   EndIf
	  EndIf
	 Next
	 If drop<>0
	  PlaySound sInvader
	  accel#=0.05-(Float#(no_invaders)/2500)
	  accel#=accel#*4
	  spEnemySpeed#=spEnemySpeed#+accel#
	  For cyc=1 To no_invaders
	   spEnemyY#(cyc)=spEnemyY#(cyc)+10;(spEnemySpeed#*10)
	   If drop>0 Then spEnemyDir(cyc)=-1 Else spEnemyDir(cyc)=1
	  Next 
	 EndIf
	EndIf
	;bugs
	If screenState<>0
	 randy=Rnd(0,1000)
	 If randy=<20 And screenState=1 Then PlaySound sInvader : DropBug(rX#(400)+Rnd(-350,350),0)
	 For cyc=1 To no_bugs
 	  If bugState(cyc)>0
	   bugY(cyc)=bugY(cyc)+bugSpeed(cyc)
	   If bugX(cyc)>spGunX#-30 And bugX(cyc)<spGunX#+30 And bugY(cyc)>spGunY#-23 And bugY(cyc)<spGunY#+23 And screenState=1 
	    PlaySound sExplode
	    CreateExplosion(spGunX#,spGunY#) 
	    PlaySound sFail : screenState=-1 : gotim=0
	   EndIf
	   If bugY(cyc)=>spGroundY Then bugState(cyc)=0 : CreateExplosion(bugX(cyc),bugY(cyc)) 
	  EndIf
	 Next  
	EndIf

    ;SCORING
    scoreTim=scoreTim-1
    If scoreTim<0 Then scoreTim=0
    gamNewScore(0)=0
    For cyc=1 To 6
     gamNewScore(0)=gamNewScore(0)+gamNewScore(cyc)
    Next
    If scoreTim=0
     For cyc=0 To 6
      If gamScore(cyc)<gamNewScore(cyc) Then gamScore(cyc)=gamScore(cyc)+1 : scoreTim=2
      If gamScore(cyc)>gamNewScore(cyc) Then gamScore(cyc)=gamScore(cyc)-1 : scoreTim=2
     Next
    EndIf
    If gamScore(0)=>gamMaxScore And screenState=1 Then PlaySound sMilestone : screenState=-2 : gotim=0

    ;EXPLOSIONS
	For v=1 To no_explodes
 	 If exState(v)>0
      exTim(v)=exTim(v)+1
      If exTim(v)>15 Then exState(v)=0
	  exY(v)=exY(v)+1
	 EndIf
	Next
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;red background
 If screenState=-1 And gotim<20
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 ;invaders
 If screenState=>0 Or gamScore(0)=>gamMaxScore Then DrawImage gSpaceship,spGunX#,spGunY#
 If screenState<>0
  For cyc=1 To no_invaders
   If spEnemy(cyc)>0 And spEnemyY#(cyc)>0 Then DrawImage gIconLarge(spEnemy(cyc)),spEnemyX#(cyc),spEnemyY#(cyc)
  Next
 EndIf
 ;bugs
 For v=1 To no_bugs
  If bugState(v)>0 Then DrawImage gIconSmall(8),bugX(v),bugY(v)
 Next
 ;bullets
 For v=1 To no_bullets
  If bullState(v)>0 Then DrawImage gBullet,bullX(v),bullY(v)
 Next
 ;explosions
 For v=1 To no_explodes
  If exState(v)>0
   If exTim(v)=>1 And exTim(v)=<3 Then DrawImage gExplode,exX(v),exY(v),0
   If exTim(v)=>4 And exTim(v)=<6 Then DrawImage gExplode,exX(v),exY(v),1
   If exTim(v)=>7 And exTim(v)=<9 Then DrawImage gExplode,exX(v),exY(v),2
   If exTim(v)=>10 And exTim(v)=<12 Then DrawImage gExplode,exX(v),exY(v),3
  EndIf
 Next 
 ;displays
 DisplayGameStatus(rX#(400),rY#(300))
 DisplayGameChar(gamWorker,rX#(115),rY#(565))
 DisplayScore(gamScore(0),rX#(635),rY#(565))
 
 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If game=1
 UpdateProject(gamProject)
 screen=50 : screenAgenda=4
Else
 screen=16
EndIf
End Function

;-----------------------------------------------------------------
;////////////////////// RELATED FUNCTIONS ////////////////////////
;----------------------------------------------------------------- 

;GET GAME SETTINGS
Function GetGameSettings()
If game>0
 gamSkill=AverageStats(gamWorker,0) : gamLimit=AverageHardwareStats(projFormat(gamProject))
 If gamDevelop=1 Then gamSkill=charDesign(gamWorker): gamLimit=hardGameplay(projFormat(gamProject))
 If gamDevelop=2 Then gamSkill=charProgram(gamWorker) : gamLimit=hardReplay(projFormat(gamProject))
 If gamDevelop=3 Then gamSkill=char2D(gamWorker) : gamLimit=hard2D(projFormat(gamProject))
 If gamDevelop=4 Then gamSkill=char3D(gamWorker) : gamLimit=hard3D(projFormat(gamProject))
 If gamDevelop=5 Then gamSkill=charSound(gamWorker) : gamLimit=hardSound(projFormat(gamProject))
 If gamDevelop=6 Then gamSkill=charMusic(gamWorker) : gamLimit=hardMusic(projFormat(gamProject))
Else
 gamSkill=100-PercentOf#(75,optDifficulty) : gamLimit=99
EndIf
End Function

;DISPLAY GAME STATUS
Function DisplayGameStatus(x,y)
 If screenState=0
  ;SetFont font(30)
  ;Outline("GET",x,y-35,0,0,0,250,Rnd(150,250),0) 
  ;Outline("READY!",x,y+20,0,0,0,250,Rnd(150,250),0) 
  DescribeGame(x,y-65)
 EndIf
 If screenState=-1
  SetFont font(30)
  Outline("GAME",x,y-35,0,0,0,Rnd(150,250),0,0) 
  Outline("OVER!",x,y+20,0,0,0,Rnd(150,250),0,0) 
 EndIf
 If screenState=-2
  SetFont font(30)
  Outline("WELL",x,y-35,0,0,0,0,Rnd(150,250),0) 
  Outline("DONE!",x,y+20,0,0,0,0,Rnd(150,250),0)
 EndIf
End Function

;DESCRIBE GAME
Function DescribeGame(x,y)
 ;title
 SetFont font(30)
 Outline(textGame$(screen-60),x,y-10,0,0,0,250,Rnd(150,250),0) 
 SetFont font(8)
 ;tetris
 If screen=61
  Outline("Slot the shapes into place",x,y+40,0,0,0,255,255,255) 
  Outline("to form complete lines!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(1),x-30,y+115 : DrawImage gControl(2),x+30,y+115
 EndIf
 ;invaders
 If screen=62
  Outline("Shoot as many invaders out of the sky as",x,y+40,0,0,0,255,255,255) 
  Outline("possible before they reach you or the ground!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(1),x-30,y+115 : DrawImage gControl(2),x+30,y+115
 EndIf
 ;breakout
 If screen=63
  Outline("Bounce the ball back against the bricks",x,y+40,0,0,0,255,255,255) 
  Outline("to score as many points as possible!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(3),x,y+115
 EndIf
 ;asteroids
 If screen=64
  Outline("Steer your spaceship away from the asteroids",x,y+40,0,0,0,255,255,255) 
  Outline("while shooting them up to score points!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(1),x-30,y+115 : DrawImage gControl(2),x+30,y+115
 EndIf
 ;moksha
 If screen=65
  Outline("Reach out left or right to collect the",x,y+40,0,0,0,255,255,255) 
  Outline("points without touching the bugs!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(2),x-60,y+115 : DrawImage gControl(1),x,y+115 : DrawImage gControl(3),x+60,y+115
 EndIf
 ;snake
 If screen=66
  Outline("Eat as many tokens as possible without coming into",x,y+40,0,0,0,255,255,255) 
  Outline("contact with the walls, the bugs - or yourself!",x,y+65,0,0,0,255,255,255) 
  DrawImage gControl(1),x-30,y+115 : DrawImage gControl(2),x+30,y+115
 EndIf
 ;sure shot
 If screen=67
  Outline("Fire from the left and the right to trap enemies",x,y+40,0,0,0,255,255,255) 
  Outline("in your crossfire without hitting each other!",x,y+65,0,0,0,255,255,255)
  DrawImage gControl(1),x-30,y+115 : DrawImage gControl(3),x+30,y+115 
 EndIf
 ;crosshair
 If screen=68
  Outline("Shoot as many enemies as",x,y+40,0,0,0,255,255,255) 
  Outline("you can with "+no_bullets+" bullets!",x,y+65,0,0,0,255,255,255)
  DrawImage gControl(3),x,y+115 
 EndIf
End Function

;DISPLAY CHARACTER STATS
Function DisplayGameChar(char,x,y)
If game=1
 If screen=61 Then DrawImage charPhoto(char),x-35,y-10 Else DrawImage charPhoto(char),x-35,y+10
 SqueezeFont(charName$(char),200,25)
 OutlineStraight(charName$(char),x-3,y-24,0,0,0,255,255,255)
 If gamDevelop=1 Then DrawMeter(1,x,y,charDesign(char),charOldDesign(char),99)
 If gamDevelop=2 Then DrawMeter(2,x,y,charProgram(char),charOldProgram(char),99)
 If gamDevelop=3 Then DrawMeter(3,x,y,char2D(char),charOld2D(char),99)
 If gamDevelop=4 Then DrawMeter(4,x,y,char3D(char),charOld3D(char),99)
 If gamDevelop=5 Then DrawMeter(5,x,y,charSound(char),charOldSound(char),99)
 If gamDevelop=6 Then DrawMeter(6,x,y,charMusic(char),charOldMusic(char),99)
 If gamDevelop=7 Then DrawMeter(1,x,y,AverageStats(char,0),AverageStats(char,0),99) 
 ;hi scores
 ;If gamHiScore(screen-60)>0
  ;SetFont font(2)
  ;OutlineStraight("Hi = "+gamHiScore(screen-60)+"pts ("+charName$(gamHiCredit(screen-60))+")",x+12,y+20,0,0,0,100,200,100)
 ;EndIf 
 If gamDevelop=>1 And gamDevelop=<6 And screen<>66
  If projHiIncrease(gamProject,gamDevelop)>0
   SetFont font(2)
   OutlineStraight("Hi = "+projHiIncrease(gamProject,gamDevelop)+"% ("+charName$(projCredit(gamProject,gamDevelop))+")",x+12,y+19,0,0,0,100,200,100)
  Else
   If gamHiScore(screen-60)>0
    SetFont font(2)
    OutlineStraight("Hi = "+gamHiScore(screen-60)+"pts ("+charName$(gamHiCredit(screen-60))+")",x+12,y+19,0,0,0,100,200,100)
   EndIf 
  EndIf
 EndIf 
EndIf
End Function

;DISPLAY SCORE
Function DisplayScore(score,x,y)
 SetFont font(20)
 OutlineStraight("Score:",x-120,y,0,0,0,255,240,0) 
 OutlineStraight(GetFigure$(score)+"pts",x,y,0,0,0,255,255,255) 
 If game=1 And screenState<0 And score>gamHiScore(screen-60)
  SetFont font(10)
  Outline("New Hi Score!",x-10,y-28,0,0,0,255,Rnd(150,250),0)
 EndIf
End Function

;SCORE POINT
Function ScorePoint(category)
 If gamDevelop=>1 And gamDevelop=<6
  gamNewScore(gamDevelop)=gamNewScore(gamDevelop)+1
 Else
  If category=>1 And category=<6
   gamNewScore(category)=gamNewScore(category)+1
  Else
   randy=Rnd(1,6)
   gamNewScore(randy)=gamNewScore(randy)+1
  EndIf
 EndIf
End Function 

;DIRECTION PRESSED?
Function DirectionPressed()
 value=0
 If KeyDown(203) Or KeyDown(205) Then value=1
 If KeyDown(200) Or KeyDown(208) Then value=1
 Return value
End Function

;FIRE BUTTON PRESSED?
Function ButtonPressed(includeKeys)
 value=0
 ;game pad buttons
 For count=1 To 12
  If JoyDown(count) Then value=1
 Next
 ;keyboard input
 If includeKeys>0
  If KeyDown(57) Or KeyDown(28) Then value=1
  If KeyDown(42) Or KeyDown(54) Then value=1
  If KeyDown(29) Or KeyDown(157) Then value=1 
 EndIf
 Return value
End Function

;FIRE BULLET
Function FireBullet(x,y)
 bullCount=bullCount+1
 If bullCount>no_bullets Then bullCount=1
 bullX(bullCount)=x : bullY(bullCount)=y
 bullState(bullCount)=1 : bullTim(bullCount)=0
End Function

;DROP BUG
Function DropBug(x,y)
 bugCount=bugCount+1
 If bugCount>no_bugs Then bugCount=1
 bugX(bugCount)=x : bugY(bugCount)=y
 bugState(bugCount)=1 : bugTim(bugCount)=0
 bugSpeed(bugCount)=Rnd(10,20)
End Function

;CREATE EXPLOSION
Function CreateExplosion(x,y)
 exCount=exCount+1
 If exCount>no_explodes Then exCount=1
 exX(exCount)=x : exY(exCount)=y
 exState(exCount)=1 : exTim(exCount)=Rnd(-2,2)
End Function