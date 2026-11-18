;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;-------------------------------- 66. SNAKE -----------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global snakeSize
Global snakeSlither#,snakeSpeed#
Dim snakeDir(100),snakeOldDir(100)
Dim snakeX(100),snakeY(100)
Dim snakeOldX(100),snakeOldY(100)
Dim snakeFoodType(100),snakeFoodState(100)
Dim snakeFoodX(100),snakeFoodY(100)
Global snakeFoodRotor

;GAME
Function Snake()
;difficulty
GetGameSettings()
snakeSpeed#=10-(Float#(gamSkill)/15)
gamLimit=gamLimit/2 : gamMaxScore=gamLimit
;reset status
snakeSize=3 : snakeSlither#=0
snakeX(1)=rX#(400)+40 : snakeY(1)=rY#(300)+100 : snakeDir(1)=2
For cyc=2 To snakeSize
 snakeDir(cyc)=snakeDir(1)
 snakeX(cyc)=snakeX(1)-(40*(cyc-1))
 snakeY(cyc)=snakeY(1)
Next
snakeFoodRotor=0
For cyc=1 To 100
 snakeFoodState(cyc)=0
 If gamDevelop=>1 And gamDevelop=<6 Then snakeFoodType(cyc)=gamDevelop Else snakeFoodType(cyc)=Rnd(1,6)
 randy=Rnd(-1,2+(gamLimit/10))
 If randy=<0 Or cyc=1 Then snakeFoodType(cyc)=8
Next
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
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
	  If screenState=0 Then screenState=1 : gotim=0 : keytim=20
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;GAME
	If screenState=1
	 ;snake control
	 If (KeyDown(200) Or JoyYDir()<0) And snakeDir(1)<>3 And snakeDir(1)<>1 Then snakeDir(1)=1 : PlaySound sMenuSelect
	 If (KeyDown(205) Or JoyXDir()>0) And snakeDir(1)<>4 And snakeDir(1)<>2 Then snakeDir(1)=2 : PlaySound sMenuSelect
	 If (KeyDown(208) Or JoyYDir()>0) And snakeDir(1)<>1 And snakeDir(1)<>3 Then snakeDir(1)=3 : PlaySound sMenuSelect
	 If (KeyDown(203) Or JoyXDir()<0) And snakeDir(1)<>2 And snakeDir(1)<>4 Then snakeDir(1)=4 : PlaySound sMenuSelect  
	 ;move snake
	 accel=40+(gotim/5)
	 If accel>100 Then accel=100
	 snakeSlither#=snakeSlither#+PercentOf#(snakeSpeed#,accel)
	 If snakeSlither#>30
	  For cyc=1 To snakeSize
	   snakeOldX(cyc)=snakeX(cyc)
	   snakeOldY(cyc)=snakeY(cyc)
	  Next
	  cyc=1
	  If snakeDir(cyc)=1 Then snakeY(cyc)=snakeY(cyc)-40
	  If snakeDir(cyc)=2 Then snakeX(cyc)=snakeX(cyc)+40
	  If snakeDir(cyc)=3 Then snakeY(cyc)=snakeY(cyc)+40
	  If snakeDir(cyc)=4 Then snakeX(cyc)=snakeX(cyc)-40
	  For cyc=2 To snakeSize
	   snakeX(cyc)=snakeOldX(cyc-1)
	   snakeY(cyc)=snakeOldY(cyc-1)
	  Next
	  snakeSlither#=0
	 EndIf
	 ;self-destruction
	 For v=2 To snakeSize
	  If snakeX(1)>snakeX(v)-20 And snakeX(1)<snakeX(v)+20 And snakeY(1)>snakeY(v)-20 And snakeY(1)<snakeY(v)+20
	   PlaySound sFail : screenState=-1 : gotim=0
	   snakeX(1)=snakeOldX(1) : snakeY(1)=snakeOldY(1)
	  EndIf
	 Next 
	 ;wall contacts
	 If snakeX(1)<rX#(400)-360 Or snakeX(1)>rX#(400)+360 Or snakeY(1)<rY#(300)-260 Or snakeY(1)>rY#(300)+220 
	  PlaySound sFail : screenState=-1 : gotim=0
	  snakeX(1)=snakeOldX(1) : snakeY(1)=snakeOldY(1)
	 EndIf
	 ;enemy contacts
	 randy=Rnd(0,200)
	 If randy=<1 Or no_enemies=0 Then SpawnFood()
	 no_enemies=0 
	 For v=1 To 100
	  If snakeFoodState(v)>0 And snakeFoodType(v)=<6 Then no_enemies=no_enemies+1
	  If snakeX(1)>snakeFoodX(v)-20 And snakeX(1)<snakeFoodX(v)+20 And snakeY(1)>snakeFoodY(v)-20 And snakeY(1)<snakeFoodY(v)+20 
	   ;eat food
	   If snakeFoodState(v)>0 And snakeFoodType(v)=<6
	    PlaySound sCollect
	    CreateExplosion(snakeFoodX(v),snakeFoodY(v))
	    ScorePoint(snakeFoodType(v))
	    snakeFoodState(v)=-1 : snakeSize=snakeSize+1
	    snakeX(snakeSize)=snakeX(snakeSize-1) : snakeY(snakeSize)=snakeY(snakeSize-1)
	    If snakeDir(1)=1 Then snakeY(snakeSize)=snakeY(snakeSize)+40
	    If snakeDir(1)=2 Then snakeX(snakeSize)=snakeX(snakeSize)-40
	    If snakeDir(1)=3 Then snakeY(snakeSize)=snakeY(snakeSize)-40
	    If snakeDir(1)=4 Then snakeX(snakeSize)=snakeX(snakeSize)+40
	    ;If snakeSize-3=>gamLimit Then PlaySound sMilestone : screenState=-2 : gotim=0
	   EndIf
	   ;die from bug
	   If snakeFoodState(v)>0 And snakeFoodType(v)=8
	    PlaySound sFail : screenState=-1 : gotim=0
	    snakeX(1)=snakeOldX(1) : snakeY(1)=snakeOldY(1)
	    ;CreateExplosion(snakeX(1),snakeY(1))
	   EndIf
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
 DrawImage gBackground,rX(400),rY(300)
 ;red background
 If screenState=-1 And gotim<20
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 DrawImage gSnakeFrame,rX(400),rY(300)
 ;food
 If screenState<>0
  For v=1 To 100
   If snakeFoodState(v)>0 Then DrawImage gIconLarge(snakeFoodType(v)),snakeFoodX(v),snakeFoodY(v)
  Next
 EndIf
 ;snake
 For count=1 To snakeSize
  cyc=(snakeSize+1)-count
  If cyc>1 Then DrawImage gSnake(5),snakeX(cyc),snakeY(cyc)
 Next
 DrawImage gSnake(snakeDir(1)),snakeX(1),snakeY(1)
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
 DisplayGameStatus(rX#(400),rY#(280))
 DisplayGameChar(gamWorker,rX#(115),rY#(585))
 DisplayScore(gamScore(0),rX#(635),rY#(570))
 ;diagnostic
 ;OutlineStraight(snakeX(1)+" / "+snakeOldX(1),rX#(400),rY#(565),255,0,0,175,175,175)
 
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

;SPAWN SNAKE FOOD
Function SpawnFood()
 ;ideal location
 Repeat
  randy=Rnd(1,19) : x=(rX#(400)-400)+(randy*40)
  randy=Rnd(1,13) : y=(rY#(300)-300)+(randy*40)
  If snakeFoodRotor=0 Then randy=Rnd(1,6) : y=(rY#(300)-300)+(randy*40)
  satisfied=1
  If x>snakeX(1)-100 And x<snakeX(1)+100 And y>snakeY(1)-100 And y<snakeY(1)+100 Then satisfied=0
  For v=1 To snakeSize
   If x>snakeX(v)-20 And x<snakeX(v)+20 And y>snakeY(v)-20 And y<snakeY(v)+20 Then satisfied=0
  Next
  For v=1 To 100
   If snakeFoodState(v)>0
    If x>snakeFoodX(v)-20 And x<snakeFoodX(v)+20 And y>snakeFoodY(v)-20 And y<snakeFoodY(v)+20 Then satisfied=0
   EndIf
  Next
 Until satisfied=1
 ;find empty slot
 ;cyc=0
 ;For count=1 To 100
  ;If snakeFoodState(count)=0 Then cyc=count : Exit
 ;Next
 snakeFoodRotor=snakeFoodRotor+1
 cyc=snakeFoodRotor
 If cyc>0
  PlaySound sInvader
  snakeFoodX(cyc)=x : snakeFoodY(cyc)=y
  snakeFoodState(cyc)=1
 EndIf
End Function