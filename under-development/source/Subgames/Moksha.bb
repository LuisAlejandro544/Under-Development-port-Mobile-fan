;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 65. MOKSHA ---------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global mokBreathe,mokHandSpeed#=1.5
Global mokLeftAnim,mokLeftTim#,mokLeftSting
Global mokRightAnim,mokRightTim#,mokRightSting
Global mokX#,mokY#
Global wheelAngle#,wheelSpeed#=2.0
Dim wheelX#(9,12),wheelY#(9,12)
Dim wheelState(9,12)
Dim wheelReplaceTim(9,12)

;GAME
Function Moksha()
;difficulty
GetGameSettings()
mokHandSpeed#=0.2+(Float#(gamSkill)/150)
wheelSpeed#=6.0-(Float#(gamLimit)/50)
wheelSpeed#=wheelSpeed#-(Float#(gamSkill)/50)
gamMaxScore=gamLimit
;reset status
mokX#=rX#(400) : mokY#=rY#(280)
mokLeftTim#=0 : mokRightTim#=0
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;wheel
For depth=1 To 9
 For spoke=1 To 12
  id=((depth-1)*12)+spoke
  If id=<gamLimit
   If gamDevelop=>1 And gamDevelop=<6 Then wheelState(depth,spoke)=gamDevelop Else wheelState(depth,spoke)=Rnd(1,6)
  Else
   wheelState(depth,spoke)=0
  EndIf
 Next
 If wheelState(depth,1)>0 Then wheelState(depth,Rnd(1,12))=8
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
	
	;GURU
	If screenState=1
	 ;left strike
	 mokLeftAnim=0
	 If KeyDown(203) Or JoyXDir()<0 Or MouseDown(1)
	  If mokLeftTim#=0 And gotim>20 Then ProduceSound(0,sSwing,22050,0.2) : mokLeftTim#=1 : mokLeftSting=1
	 EndIf
	 If mokLeftTim#=>1
	  If mokLeftTim#<3 Then mokLeftAnim=1
	  If mokLeftTim#=>3 And mokLeftTim#=<6 Then mokLeftAnim=2
	  If mokLeftAnim=2 And mokLeftSting>0 Then FindHandHits(mokX#-70,mokY#+33)
	  If mokLeftTim#>6 Then mokLeftAnim=1
	  If mokLeftTim#=>9 Then mokLeftTim#=0
	  If mokLeftTim#=>1 Then mokLeftTim#=mokLeftTim#+mokHandSpeed#
	 EndIf
	 ;right strike
	 mokRightAnim=0
	 If KeyDown(205) Or JoyXDir()>0 Or MouseDown(2)
	  If mokRightTim#=0 And gotim>20 Then ProduceSound(0,sSwing,22050,0.2) : mokRightTim#=1 : mokRightSting=1
	 EndIf 
	 If mokRightTim#=>1
	  If mokRightTim#<3 Then mokRightAnim=1
	  If mokRightTim#=>3 And mokRightTim#=<6 Then mokRightAnim=2
	  If mokRightAnim=2 And mokRightSting>0 Then FindHandHits(mokX#+70,mokY#+33)
	  If mokRightTim#>6 Then mokRightAnim=1
	  If mokRightTim#=>9 Then mokRightTim#=0
	  If mokRightTim#=>1 Then mokRightTim#=mokRightTim#+mokHandSpeed#
	 EndIf 
	 ;breathing
	 mokBreathe=mokBreathe+1
	 If mokBreathe>40 Then mokBreathe=0
	 ;wheel
	 If screenState=1 Then wheelAngle#=wheelAngle#+wheelSpeed#
	 wheelAngle#=CleanAngle#(wheelAngle#)
	 For depth=1 To 8
      For spoke=1 To 12
       wheelReplaceTim(depth,spoke)=wheelReplaceTim(depth,spoke)-1
       If wheelReplaceTim(depth,spoke)<0 Then wheelReplaceTim(depth,spoke)=0
       If wheelState(depth,spoke)=0 And wheelReplaceTim(depth,spoke)=0
        wheelState(depth,spoke)=wheelState(depth+1,spoke)
        wheelState(depth+1,spoke)=0
        If wheelState(depth,spoke)=>1 And wheelState(depth,spoke)=<6 Then ProduceSound(0,sInvader,0,0.5)
        If wheelState(depth,spoke)=8 Then ProduceSound(0,sInvader,0,1.0)
       EndIf
      Next
     Next
     ;bypass bugs
     For depth=2 To 9
      For spoke=1 To 12
       If wheelState(depth,spoke)>0 And wheelState(depth-1,spoke)=8
        For v=1 To 12
         If wheelState(1,v)=0 And wheelReplaceTim(1,v)=0
          If wheelState(depth,spoke)=8 Then vol#=1.0 Else vol#=0.5
          ProduceSound(0,sInvader,0,vol#)
          wheelState(1,v)=wheelState(depth,spoke)
          wheelState(depth,spoke)=0
          Exit
         EndIf
        Next
       EndIf
      Next
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
    ;count available enemies
    If screenState=1 
     no_enemies=0
     For depth=1 To 9
      For spoke=1 To 12
       If wheelState(depth,spoke)=>1 And wheelState(depth,spoke)=<6 Then no_enemies=no_enemies+1
      Next
     Next
     If no_enemies=0 Then PlaySound sMilestone : screenState=-2 : gotim=0
	EndIf

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
 ;wheel
 If screenState<>0
  For depth=1 To 4
   range=45+(depth*45)
   For spoke=1 To 12
    If wheelState(depth,spoke)>0
     wheelX#(depth,spoke)=mokX#+Cos((wheelAngle#-90)+(30*spoke))*range
     wheelY#(depth,spoke)=mokY#+Sin((wheelAngle#-90)+(30*spoke))*range
     DrawImage gIconLarge(wheelState(depth,spoke)),wheelX#(depth,spoke),wheelY#(depth,spoke)
    EndIf
   Next
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
 ;guru
 If mokBreathe>20 Then frame=5 Else frame=6
 If mokLeftAnim=1 And mokRightAnim=0 Then frame=7
 If mokLeftAnim=2 And mokRightAnim=0 Then frame=8
 If mokLeftAnim=2 And mokRightAnim=1 Then frame=9
 If mokLeftAnim=0 And mokRightAnim=1 Then frame=2
 If mokLeftAnim=0 And mokRightAnim=2 Then frame=3
 If mokLeftAnim=1 And mokRightAnim=2 Then frame=4
 If mokLeftAnim=1 And mokRightAnim=1 Then frame=1
 If mokLeftAnim=2 And mokRightAnim=2 Then frame=0
 DrawImage gGuru,mokX#,mokY#,frame
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

;FIND HAND CONNECTIONS
Function FindHandHits(x,y)
 ;find closest
 v=0 : hi=999
 For spoke=1 To 12
  If (x<400 And wheelX#(1,spoke)<400) Or (x>400 And wheelX#(1,spoke)>400)
   distance=GetDiff#(y,wheelY#(1,spoke));GetDistance#(x,y,wheelX#(1,spoke),wheelY#(1,spoke))
   If wheelState(1,spoke)>0 And distance<hi Then v=spoke : hi=distance
  EndIf
 Next
 ;act on it
 If v>0
  If x>wheelX#(1,v)-20 And x<wheelX#(1,v)+20 And y>wheelY#(1,v)-20 And y<wheelY#(1,v)+20
    If wheelState(1,v)=>1 And wheelState(1,v)=<6
     ;score point
     ProduceSound(0,sGong,22050,0.5) : PlaySound sCollect
     CreateExplosion(wheelX#(1,v),wheelY#(1,v))
     ScorePoint(wheelState(1,v))
     wheelState(1,v)=0
     wheelReplaceTim(1,v)=20
     accel#=0.15-(Float#(gamLimit-20)/800)
     wheelSpeed#=wheelSpeed#+accel#
     If x<400 Then mokLeftSting=0 Else mokRightSting=0
    Else
     ;hit bug
     ProduceSound(0,sGong,22050,0)
     CreateExplosion(wheelX#(1,v),wheelY#(1,v))
     PlaySound sFail
     screenState=-1 : gotim=0
    EndIf
  EndIf
 EndIf
End Function