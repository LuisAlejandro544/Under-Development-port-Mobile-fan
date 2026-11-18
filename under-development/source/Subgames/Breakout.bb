;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------- 63. BREAKOUT ---------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global batX#,batY#,batGround#
Global batSpeed#,batSize
Global ballX#,ballY#
Global ballOldX#,ballOldY#
Global ballSpeedX#,ballSpeedY#
Global ballA#,ballOldA#,ballSpeed#
Global ballReleased
Global no_blocks=99
Dim blockX(100),blockY(100)
Dim blockType(100)
Dim blockHP(100)

;GAME
Function Breakout()
;difficulty
GetGameSettings()
batSize=gamSkill/6
gamMaxScore=gamLimit
;reset blocks
no_blocks=gamLimit
gamMaxScore=no_blocks
batGround#=rY#(300)+220
x=rX#(400)-291 : y=rY#(300)-230
For cyc=1 To no_blocks
 blockX(cyc)=x : blockY(cyc)=y
 If gamDevelop=>1 And gamDevelop=<6 Then blockType(cyc)=gamDevelop Else blockType(cyc)=Rnd(1,6)
 blockHP(cyc)=1 
 randy=Rnd(0,10)
 If randy=<1 Then blockType(cyc)=8 : blockHP(cyc)=Rnd(2,3) 
 x=x+45
 If x>rX#(400)+309 Then x=rX#(400)-291 : y=y+45
Next
;reset status
batX#=rX#(400) : batY#=batGround#-15
ballX#=batX# : ballY#=batY#-19
ballSpeed#=(175-no_blocks)/10
ballReleased=0
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
	
	;BAT
	If screenState=1
	 ;bat control
	 batX#=MouseX()
	 If batX#<rX#(400)-300 Then batX#=rX#(400)-300
	 If batX#>rX#(400)+300 Then batX#=rX#(400)+300
	 ;release ball
	 If ballReleased=0
	  ballX#=batX# : ballY#=batY#-19
	  If MouseDown(1) And screenState=1 And keytim=0 Then PlaySound sMenuSelect : ballA#=0 : ballReleased=1
	 EndIf
	EndIf
	
    ;BALL MOVEMENT
	If screenState=1 And ballReleased>0
	 ballA#=CleanAngle#(ballA#) : ballOldA#=ballA#
	 ballOldX#=ballX# : ballOldY#=ballY#
	 ballX#=ballX#+(Cos(ballA#-90)*ballSpeed#)
	 ballY#=ballY#+(Sin(ballA#-90)*ballSpeed#)
	 ;ballY#=ballY#+2
	 ;wall impacts
	 If ballX#<rX#(400)-310
	  PlaySound sBall : ballX#=rX#(400)-310 : ballA#=0-ballA# : ballA#=ballA#+Rnd(-10,10)
	  If ballOldA#>90 And ballOldA#<180 Then ballA#=ballA#-10
	  If ballOldA#>180 And ballOldA#<270 Then ballA#=ballA#+10
	 EndIf
	 If ballX#>rX#(400)+310
	  PlaySound sBall : ballX#=rX#(400)+310 : ballA#=0-ballA# : ballA#=ballA#+Rnd(-10,10) 
	  If ballOldA#>90 And ballOldA#<180 Then ballA#=ballA#-10
	  If ballOldA#>180 And ballOldA#<270 Then ballA#=ballA#+10
	 EndIf
	 If ballY#<rY#(300)-255 Then PlaySound sBall : ballY#=rY#(300)-255 : ballA#=180-ballA# : ballA#=ballA#+Rnd(-10,10)
	 If ballY#>batGround# Then PlaySound sFail : screenState=-1 : gotim=0
	 ;bat impacts
	 range=6+(6+((batSize*7)/2))
	 If ballX#>batX#-range And ballX#<batX#+range And ballY#>batY#-19 And ballY#<batY#+10
	  PlaySound sBall
	  ballY#=batY#-19
	  distance#=GetDiff#(ballX#,batX#)
	  factor#=GetPercent#(distance#,range)
	  If ballX#<batX# Then ballA#=-PercentOf#(45,factor#) Else ballA#=PercentOf#(45,factor#)
	  ballSpeed#=ballSpeed#+0.25
	 EndIf
	 ;block impacts 
	 For v=1 To no_blocks
	  If blockType(v)>0
	   If ballX#>blockX(v)-25 And ballX#<blockX(v)+25 And ballY#>blockY(v)-25 And ballY#<blockY(v)+25 
	    PlaySound sBall
	    ballA#=Rnd(100,260)
	    blockHP(v)=blockHP(v)-1
	    If blockHP(v)=<0
	     ProduceSound(0,sGong,22050,0.5) : PlaySound sCollect
	     CreateExplosion(blockX(v),blockY(v))
	     ScorePoint(blockType(v))
	     blockType(v)=0
	    Else
	     CreateExplosion(ballX#,ballY#)
	    EndIf
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
 DrawImage gBackground,rX#(400),rY#(300)
 ;red background
 If screenState=-1 And gotim<20
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 ;bat
 DrawImage gBat(1),(batX#-((batSize*7)/2))-6,batY#
 x=(batX#-((batSize*7)/2))+3
 For count=1 To batSize
  DrawImage gBat(2),x,batY#
  x=x+7
 Next
 DrawImage gBat(3),(batX#+((batSize*7)/2))+6,batY#
 ;blocks
 If screenState<>0
  For cyc=1 To no_blocks
   If blockType(cyc)>0
    DrawImage gIconLarge(blockType(cyc)),blockX(cyc),blockY(cyc)
   EndIf
  Next
 EndIf
 ;frame
 DrawImage gFrame,rX#(400),rY#(300)-38
 ;ball
 If screenState=>0 Then DrawImage gBall,ballX#,ballY# 
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