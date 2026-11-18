;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 67. SURE SHOT ---------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global sureEnemySpeed#
Dim sureX(2),sureY(2)
Dim sureAnim(2),sureAnimTim(2),sureAnimFrame(2)
Dim sureFire(2),sureHP(2)
;enemies
Dim enemyType(100),enemySize(100)
Dim enemyState(100),enemyHP(100)
Dim enemyX#(100),enemyY#(100)
Dim enemyA#(100),enemySpeed#(100)

;GAME
Function SureShot()
;difficulty
GetGameSettings()
sureEnemySpeed#=Float#(120-gamSkill)/15
gamLimit=gamLimit/2 : gamMaxScore=gamLimit
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;reset players
sureX(1)=rX#(400)-300 : sureY(1)=rY#(300)
sureX(2)=rX#(400)+300 : sureY(2)=rY#(300)
For cyc=1 To 2
 sureHP(cyc)=5+(gamSkill/10)
 sureAnim(cyc)=0 : sureAnimTim(cyc)=0 : sureAnimFrame(cyc)=0
Next
;enemies
For cyc=1 To gamLimit
 If gamDevelop=>1 And gamDevelop=<6 Then enemyType(cyc)=gamDevelop Else enemyType(cyc)=Rnd(1,6)
 randy=Rnd(0,10)
 If randy=<1 Then enemyType(cyc)=8
 If enemyType(cyc)=8 Then enemyHP(cyc)=Rnd(2,5) Else enemyHP(cyc)=1
 enemyState(cyc)=0
Next
For cyc=1 To 4
 SpawnEnemy()
Next
;explosions
exCount=0
For v=1 To no_explodes
 exState(v)=0 : exTim(v)=0
Next
no_bullets=20 : bullCount=0
For v=1 To no_bullets
 bullState(v)=0 : bullTim(v)=0
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
	 If KeyDown(1) Or MouseDown(1) Or DirectionPressed() Or ButtonPressed(1) Or (screenState<0 And gotim>200) Or (screenState=0 And gotim>500)
	  If screenState<0 And gamScore(0)=gamNewScore(0) And gotim>50 Then go=1
	  If screenState=0 Then screenState=1 : gotim=0 : keytim=20
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;GAME
	sureFire(1)=0 : sureFire(2)=0 
	If screenState<>0
	 ;soldiers
	 If screenState=1 And gotim>20
	  If MouseDown(1) Or KeyDown(203) Or KeyDown(42) Or KeyDown(29) Then sureFire(1)=1
	  If MouseDown(2) Or KeyDown(205) Or KeyDown(54) Or KeyDown(157) Then sureFire(2)=1
	 EndIf
	 For cyc=1 To 2
	  If sureAnim(cyc)=0 ;resting
	   If sureAnimTim(cyc)>30 Then sureAnimFrame(cyc)=1 Else sureAnimFrame(cyc)=0
	   If sureAnimTim(cyc)>60 Then sureAnimTim(cyc)=0
	   If sureFire(cyc)>0 Then sureAnim(cyc)=10 : sureAnimTim(cyc)=0
	  EndIf
	  If sureAnim(cyc)=1 ;hurt
	   sureAnimFrame(cyc)=5
	   If sureAnimTim(cyc)>15 Then sureAnimFrame(cyc)=4
	   If sureAnimTim(cyc)>25 Then sureAnim(cyc)=0 : sureAnimTim(cyc)=0
	  EndIf
	  If sureAnim(cyc)=2 ;dying
	   sureAnimFrame(cyc)=10
	   If sureAnimTim(cyc)>7
	    sureAnimFrame(cyc)=11
	    If screenState=1 Then PlaySound sFail : screenState=-1 : gotim=0
	   EndIf
	  EndIf
	  If sureAnim(cyc)=10 ;firing
	   If sureAnimTim(cyc)>4 Then sureAnimFrame(cyc)=3 Else sureAnimFrame(cyc)=2
	   If sureAnimTim(cyc)=1
	    ProduceSound(0,sShot(Rnd(1,2)),22050,0.5)
	    If cyc=1 Then FireBullet(sureX(cyc)+43,sureY(cyc)+Rnd(14,18)) : bullSpeed(bullCount)=5
	    If cyc=2 Then FireBullet(sureX(cyc)-43,sureY(cyc)+Rnd(14,18)) : bullSpeed(bullCount)=-5
	   EndIf
	   If sureAnimTim(cyc)>8
	    sureAnimTim(cyc)=0
	    If sureFire(cyc)=0 Then sureAnim(cyc)=0
	   EndIf
	  EndIf
	  sureAnimTim(cyc)=sureAnimTim(cyc)+1
	 Next
	 ;bullets
	 For cyc=1 To no_bullets
 	  If bullState(cyc)>0
      For count=1 To 3
	   bullX(cyc)=bullX(cyc)+bullSpeed(cyc)
	   ;enemy contacts
	   For v=1 To gamLimit
	    If enemyState(v)>0 And bullState(cyc)>0
	     range=20
	     If bullX(cyc)>enemyX#(v)-range And bullX(cyc)<enemyX#(v)+range And bullY(cyc)>enemyY#(v)-range And bullY(cyc)<enemyY#(v)+range
	      enemyHP(v)=enemyHP(v)-1 : bullState(cyc)=0 
	      If enemyHP(v)>0
	       ProduceSound(0,sWound,22050,0) : ProduceSound(0,sPain,5000,0)
	       CreateExplosion(bullX(cyc),bullY(cyc)) 
	       enemyX#(v)=enemyX#(v)+bullSpeed(cyc)
	      Else
	       ProduceSound(0,sExplode,22050,0.5) : ProduceSound(0,sCollect,0,0.5)
	       CreateExplosion(enemyX#(v),enemyY#(v)) 
	       ScorePoint(enemyType(v))
	       enemyState(v)=-1
	      EndIf
	     EndIf
	    EndIf
	   Next
	   ;soldier contacts
	   v=0
	   If bullX(cyc)<sureX(1)+10 And sureAnim(1)<>2 Then v=1  
	   If bullX(cyc)>sureX(2)-10 And sureAnim(2)<>2 Then v=2  
	   If v>0
	    ProduceSound(0,sWound,22050,0) : ProduceSound(0,sPain,22050,0)
	    CreateExplosion(bullX(cyc),bullY(cyc)) 
	    sureX(v)=sureX(v)+(bullSpeed(cyc)/10) : sureHP(v)=sureHP(v)-1
	    If sureHP(v)>0 Then sureAnim(v)=1 Else sureAnim(v)=2
	    If sureAnim(v)=2 Then ProduceSound(0,sDie,22050,1)
	    sureAnimTim(v)=0 : bullState(cyc)=0
	   EndIf
	   ;other bullets
	   For v=1 To no_bullets
	    If cyc<>v And bullState(cyc)>0 And bullState(v)>0
	     randy=Rnd(0,3)
	     If randy=0 And bullX(cyc)>bullX(v)-5 And bullX(cyc)<bullX(v)+5 And bullY(cyc)>bullY(v)-1 And bullY(cyc)<bullY(v)+1
	      ProduceSound(0,sRicochet,22050,0)
	      CreateExplosion(bullX(cyc),bullY(cyc))
	      ;CreateExplosion(bullX(v),bullY(v))
	      bullState(cyc)=0 : bullState(v)=0
	     EndIf
	    EndIf
	   Next
	  Next
	  EndIf
	 Next
	EndIf
	;enemies
	If screenState=1
	 randy=Rnd(0,100)
	 If randy=<1 Or no_enemies=0 Then SpawnEnemy()
	 no_enemies=0 
	 For cyc=1 To gamLimit
	  If enemyState(cyc)>0
	   no_enemies=no_enemies+1
	   enemyA(cyc)=CleanAngle#(enemyA(cyc))
	   enemyX#(cyc)=enemyX#(cyc)+(Cos(enemyA(cyc)-90)*enemySpeed#(cyc))
	   enemyY#(cyc)=enemyY#(cyc)+(Sin(enemyA(cyc)-90)*enemySpeed#(cyc))
	   If enemyY#(cyc)>rY#(550) And enemyA#(cyc)=180 Then enemyA#(cyc)=0 : enemySpeed#(cyc)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#*2)
	   If enemyY#(cyc)<0 And enemyA#(cyc)=0 Then enemyA#(cyc)=180 : enemySpeed#(cyc)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#*2)
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
 redder=0
 If screenState=1 And (sureAnimFrame(1)=5 Or sureAnimFrame(2)=5) Then redder=1
 If screenState=-1 And gotim<20 Then redder=1
 If redder>0
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 ;soldiers
 For cyc=1 To 2
  DrawImage gSoldier(cyc),sureX(cyc),sureY(cyc),sureAnimFrame(cyc)
 Next
 ;enemies
 If screenState<>0
  For cyc=1 To gamLimit
   If enemyState(cyc)>0 Then DrawImage gIconLarge(enemyType(cyc)),enemyX#(cyc),enemyY#(cyc)
  Next
 EndIf
 ;bullets
 For v=1 To no_bullets
  If bullState(v)>0 
   If bullSpeed(v)>0 Then DrawImage gSureBullet(1),bullX(v),bullY(v)
   If bullSpeed(v)<0 Then DrawImage gSureBullet(2),bullX(v),bullY(v)
  EndIf
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

;SPAWN ENEMY
Function SpawnEnemy()
 cyc=0
 For count=1 To gamLimit
  If enemyState(count)=0 Then cyc=count : Exit
 Next
 If cyc>0
  ;PlaySound sInvader
  enemyX#(cyc)=rX#(400)+Rnd(-200,200) : enemyY#(cyc)=0 : enemyA(cyc)=180
  enemySpeed#(cyc)=Rnd#(sureEnemySpeed#/2,sureEnemySpeed#*2)
  enemyState(cyc)=1
 EndIf
End Function