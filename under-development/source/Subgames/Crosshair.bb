;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 68. CROSSHAIR ---------------------------------
;//////////////////////////////////////////////////////////////////////////////
Global crossX,crossY

Function Crosshair()
;difficulty
GetGameSettings()
gamLimit=gamLimit/2 : gamMaxScore=gamLimit
sureEnemySpeed#=Float#(225-gamSkill)/25
no_bullets=gamLimit;+PercentOf#(gamLimit,gamSkill/10)
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;enemies
crossX=rX#(400) : crossY=rY#(300)+125
For cyc=1 To gamLimit
 If gamDevelop=>1 And gamDevelop=<6 Then enemyType(cyc)=gamDevelop Else enemyType(cyc)=Rnd(1,6)
 randy=Rnd(0,20)
 If randy=<1 Then enemyType(cyc)=8
 If enemyType(cyc)=8 Then enemyHP(cyc)=Rnd(2,3) Else enemyHP(cyc)=1
 enemyState(cyc)=0
Next
;explosions
exCount=0
For v=1 To no_explodes
 exState(v)=0 : exTim(v)=0
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
	  For count=0 To 6
	   gamNewScore(count)=gamNewScore(count)/2
	  Next
	 EndIf
	 ;proceed
	 If KeyDown(1) Or MouseDown(1) Or DirectionPressed() Or ButtonPressed(1) Or (screenState<0 And gotim>200) Or (screenState=0 And gotim>300)
	  If screenState<0 And gamScore(0)=gamNewScore(0) And gotim>50 Then go=1
	  If screenState=0 Then SpawnCrosshairEnemy() : screenState=1 : gotim=0 : keytim=20
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;GAME
	If screenState=1
	 ;gun
	 crossX=MouseX() : crossY=MouseY()
	 If keytim=12 Then ProduceSound(0,sReload,22050,0.25)
	 If keytim=0 And no_bullets=0 Then PlaySound sFail : screenState=-1 : gotim=0
	 If MouseDown(1) And keytim=0
	  ProduceSound(0,sShot(Rnd(1,2)),22050,0) : keytim=40-(gamSkill/5)
	  CreateExplosion(crossX,crossY) 
	  For v=1 To gamLimit
	    If enemyState(v)>0
	     range=10+(10*enemySize(v))
	     If crossX>enemyX#(v)-range And crossX<enemyX#(v)+range And crossY>enemyY#(v)-range And crossY<enemyY#(v)+range 
	     enemyHP(v)=enemyHP(v)-1
	      If enemyHP(v)>0
	       ProduceSound(0,sWound,22050,0.5+(0.5*enemySize(v))) : ProduceSound(0,sPain,5000,0.5+(0.5*enemySize(v)))
	       enemyA(v)=Rnd(0,359) : enemySpeed#(v)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#)
	      Else
	       ProduceSound(0,sExplode,22050,0.5+(0.5*enemySize(v))) : ProduceSound(0,sCollect,0,0.5) 
	       ScorePoint(enemyType(v))
	       enemyState(v)=-1
	      EndIf
	     EndIf
	    EndIf
	  Next
	  no_bullets=no_bullets-1
	  If no_bullets<0 Then no_bullets=0
	 EndIf
	 ;enemy movement
	 randy=Rnd(0,50)
	 If randy=<1 Or no_enemies=0 Then SpawnCrosshairEnemy()
	 no_enemies=0 : viables=0
	 For cyc=1 To gamLimit
	  If enemyState(cyc)>0
	   no_enemies=no_enemies+1
	   randy=Rnd(0,100)
	   If randy=<1
	    enemyA(cyc)=Rnd(0,359)
	    If enemySpeed#(cyc)>0 Then enemySpeed#(cyc)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#*2)
	   EndIf
	   enemyA(cyc)=CleanAngle#(enemyA(cyc))
	   If enemySpeed#(cyc)>0
	    enemyX#(cyc)=enemyX#(cyc)+(Cos(enemyA(cyc)-90)*enemySpeed#(cyc))
	    enemyY#(cyc)=enemyY#(cyc)+(Sin(enemyA(cyc)-90)*enemySpeed#(cyc))
	   EndIf
	   randy=Rnd(0,200)
	   If randy=0 And enemySpeed#(cyc)=0 Then ProduceSound(0,sSwing,22050,0.25) : enemyState(cyc)=0
	   If enemyX#(cyc)<-20 Or enemyX#(cyc)>rX#(800)+20 Or enemyY#(cyc)<-20 Or enemyY#(cyc)>rY#(600)+20 Then enemyState(cyc)=0
	  EndIf
	  If enemyState(cyc)=>0 And enemyType(cyc)=>1 And enemyType(cyc)=<6 Then viables=viables+1
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
    If (gamScore(0)=>gamMaxScore Or viables=0) And screenState=1 Then PlaySound sMilestone : screenState=-2 : gotim=0

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
 redder=0
 If screenState=-1 And gotim<20 Then redder=1
 If redder>0
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 ;displays
 DisplayGameStatus(rX#(400),rY#(300))
 DisplayGameChar(gamWorker,rX#(115),rY#(565))
 DisplayScore(gamScore(0),rX#(635),rY#(565))
 SetFont font(20)
 DrawImage gBullet,rX#(400)+10,rY#(565)
 OutlineStraight(no_bullets,rX#(400)+20,rY#(565),255,0,0,175,175,175)
 ;enemies
 If screenState<>0
  For cyc=1 To gamLimit
   If enemyState(cyc)>0 
    If enemySize(cyc)=0 Then DrawImage gIconSmall(enemyType(cyc)),enemyX#(cyc),enemyY#(cyc)
    If enemySize(cyc)=1 Then DrawImage gIconLarge(enemyType(cyc)),enemyX#(cyc),enemyY#(cyc)
   EndIf
  Next
 EndIf
 ;explosions
 For v=1 To no_explodes
  If exState(v)>0
   If exTim(v)=>1 And exTim(v)=<3 Then DrawImage gExplode,exX(v),exY(v),0
   If exTim(v)=>4 And exTim(v)=<6 Then DrawImage gExplode,exX(v),exY(v),1
   If exTim(v)=>7 And exTim(v)=<9 Then DrawImage gExplode,exX(v),exY(v),2
   If exTim(v)=>10 And exTim(v)=<12 Then DrawImage gExplode,exX(v),exY(v),3
  EndIf
 Next 
 ;crosshair
 DrawImage gCrosshair,crossX,crossY 
 
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

;SPAWN CROSSHAIR ENEMY
Function SpawnCrosshairEnemy()
 cyc=0
 For count=1 To gamLimit
  If enemyState(count)=0 Then cyc=count : Exit
 Next
 If cyc>0
  ;PlaySound sInvader
  randy=Rnd(0,2)
  If randy=0 Then enemySpeed#(cyc)=0 Else enemySpeed#(cyc)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#*2)
  If enemySpeed#(cyc)>0
   randy=Rnd(1,4)
   If randy=1 Then enemyX#(cyc)=Rnd(50,750) : enemyY#(cyc)=0 : enemyA(cyc)=Rnd(100,260)
   If randy=2 Then enemyX#(cyc)=Rnd(50,750) : enemyY#(cyc)=600 : enemyA(cyc)=Rnd(-80,80)
   If randy=3 Then enemyX#(cyc)=0 : enemyY#(cyc)=Rnd(50,550) : enemyA(cyc)=Rnd(10,170)
   If randy=4 Then enemyX#(cyc)=800 : enemyY#(cyc)=Rnd(50,550) : enemyA(cyc)=Rnd(190,350)
  Else
   ProduceSound(0,sSwing,22050,0.25)
   enemyX#(cyc)=Rnd(100,700) : enemyY#(cyc)=Rnd(100,500)
  EndIf
  enemyX#(cyc)=rX#(enemyX#(cyc)) : enemyY#(cyc)=rY#(enemyY#(cyc))
  randy=Rnd(0,2)
  If randy=0 Then enemySize(cyc)=0 Else enemySize(cyc)=1
  If enemyType(cyc)=8 Then enemyHP(cyc)=Rnd(2,2) Else enemyHP(cyc)=1
  enemyState(cyc)=1
 EndIf
End Function