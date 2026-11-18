;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 64. ASTEROIDS ---------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global astShipSize=10
Global astShipX#,astShipY#
Global astShipA,astShipDir
Global astHeadX#,astHeadY#
Global astLeftX#,astLeftY#
Global astRightX#,astRightY#
Global astShipSpeed#=10,astShipAccel=0
Global no_roids=99
Dim astSize(100)
Dim astType(100)
Dim astState(100)
Dim astHP(100)
Dim astX#(100),astY#(100)
Dim astA(100),astSpeed#(100)
Dim astImpactTim(100)

;GAME
Function Asteroids()
;difficulty
GetGameSettings()
astShipSpeed#=1+(Float#(gamSkill)/10)
gamLimit=gamLimit/2 : gamMaxScore=gamLimit
;reset status
astShipX=rX#(400) : astShipY=rY#(300)+110
astShipA=Rnd(0,359) : astShipDir=1
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;asteroids
no_roids=gamLimit
rotor=0
For cyc=1 To no_roids
 rotor=rotor+1
 If rotor>5 Then rotor=1
 If rotor=1 Then astSize(cyc)=1 Else astSize(cyc)=0
 If gamDevelop=>1 And gamDevelop=<6 Then astType(cyc)=gamDevelop Else astType(cyc)=Rnd(1,6)
 astHP(cyc)=1
 randy=Rnd(0,10)
 If randy=<1 Then astType(cyc)=8 : astHP(cyc)=Rnd(2,3)
 astState(cyc)=0
Next
;explosions
exCount=0
For v=1 To no_explodes
 exState(v)=0 : exTim(v)=0
Next
no_bullets=10 : bullCount=0
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
	  CreateExplosion(astShipX#,astShipY#)  
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
	 ;ship control
	 If KeyDown(203) Or JoyXDir()<0 Then astShipA=astShipA-5;(astShipSpeed#/2)
	 If KeyDown(205) Or JoyXDir()>0 Then astShipA=astShipA+5;(astShipSpeed#/2)
	 astShipA=CleanAngle#(astShipA)
	 astShipAccel=astShipAccel-1
	 If (KeyDown(200) Or JoyYDir()<0) And astShipDir=1 Then astShipAccel=astShipAccel+3 
	 If (KeyDown(208) Or JoyYDir()>0) And astShipDir=-1 Then astShipAccel=astShipAccel+3
	 If (KeyDown(200) Or JoyYDir()<0) And astShipDir=-1 Then astShipAccel=astShipAccel-2
	 If (KeyDown(208) Or JoyYDir()>0) And astShipDir=1 Then astShipAccel=astShipAccel-2  
	 If astShipAccel>100 Then astShipAccel=100
	 If astShipAccel<0 Then astShipAccel=0  
	 If (KeyDown(200) Or JoyYDir()<0) And astShipAccel=0 Then astShipDir=1
	 If (KeyDown(208) Or JoyYDir()>0) And astShipAccel=0 Then astShipDir=-1
	 If astShipDir=1 
	  astShipX#=astShipX#+(Cos(astShipA-90)*PercentOf#(astShipSpeed#,astShipAccel))
	  astShipY#=astShipY#+(Sin(astShipA-90)*PercentOf#(astShipSpeed#,astShipAccel))
	 Else
	  astShipX#=astShipX#+(Cos((astShipA-90)+180)*PercentOf#(astShipSpeed#/2,astShipAccel))
	  astShipY#=astShipY#+(Sin((astShipA-90)+180)*PercentOf#(astShipSpeed#/2,astShipAccel))
	 EndIf
	 If astShipX#<0 Then astShipX#=0 
	 If astShipX#>rX#(800) Then astShipX#=rX#(800)
	 If astShipY#<0 Then astShipY#=0 
	 If astShipY#>rY#(600) Then astShipY#=rY#(600)   
	 If ButtonPressed(1) And keytim=0
	  ProduceSound(0,sFire,22050,0.5) : keytim=25-(gamSkill/9)
	  FireBullet(astHeadX#,astHeadY#)
	  bullA(bullCount)=astShipA : bullSpeed(bullCount)=astShipSpeed#
	 EndIf
	 ;bullets
	 For cyc=1 To no_bullets
 	  If bullState(cyc)>0
	   bullX(cyc)=bullX(cyc)+(Cos(bullA(cyc)-90)*bullSpeed(cyc))
	   bullY(cyc)=bullY(cyc)+(Sin(bullA(cyc)-90)*bullSpeed(cyc))
	   For v=1 To no_roids
	    If astState(v)>0 And bullState(cyc)>0
	     If astSize(v)>0 Then range=22 Else range=11
	     If bullX(cyc)>astX#(v)-range And bullX(cyc)<astX#(v)+range And bullY(cyc)>astY#(v)-range And bullY(cyc)<astY#(v)+range
	      ProduceSound(0,sExplode,22050,0.5+(0.5*astSize(v)))
	      astHP(v)=astHP(v)-1 : astA(v)=bullA(cyc) : bullState(cyc)=0 
	      If astHP(v)>0 Then CreateExplosion(bullX(cyc),bullY(cyc)) 
	      If astHP(v)=<0
	       ProduceSound(0,sCollect,0,0.5)
	       CreateExplosion(astX#(v),astY#(v)) 
	       If astSize(v)>0
	        For count=1 To 4 
	         ReleaseAsteroid(astX#(v),astY(v)) 
	        Next
	       EndIf
	       ScorePoint(astType(v))
	       astState(v)=-1
	      EndIf
	     EndIf
	    EndIf
	   Next
	   If bullX(cyc)<0 Or bullX(cyc)>rX#(800) Or bullY(cyc)<0 Or bullY(cyc)>rY#(600) Then bullState(cyc)=0  
	  EndIf
	 Next
	 ;asteroids
	 randy=Rnd(0,400)
	 If randy=<1 Or no_enemies=0 Then SpawnAsteroid()
	 no_enemies=0 
	 For cyc=1 To no_roids
 	  If astState(cyc)>0
       no_enemies=no_enemies+1
       astA(cyc)=CleanAngle#(astA(cyc))
	   astX#(cyc)=astX#(cyc)+(Cos(astA(cyc)-90)*astSpeed#(cyc))
	   astY#(cyc)=astY#(cyc)+(Sin(astA(cyc)-90)*astSpeed#(cyc))
	   If astX#(cyc)<0 Then PlaySound sInvader : astX#(cyc)=0 : astA(cyc)=0-astA(cyc) : astSpeed#(cyc)=Rnd#(1.0,7.0)
	   If astX#(cyc)>rX#(800) Then PlaySound sInvader : astX#(cyc)=800 : astA(cyc)=0-astA(cyc) : astSpeed#(cyc)=Rnd#(1.0,7.0)
	   If astY#(cyc)<0 Then PlaySound sInvader : astY#(cyc)=0 : astA(cyc)=180-astA(cyc) : astSpeed#(cyc)=Rnd#(1.0,7.0)
	   If astY#(cyc)>rY#(600) Then PlaySound sInvader : astY#(cyc)=600 : astA(cyc)=180-astA(cyc) : astSpeed#(cyc)=Rnd#(1.0,7.0)
	   ;collide with other asteroids
	   astImpactTim(cyc)=astImpactTim(cyc)-1
	   If astImpactTim(cyc)<0 Then astImpactTim(cyc)=0
	   For v=1 To no_roids
	    If cyc<>v And astState(v)>0 And astImpactTim(cyc)=0 And astImpactTim(v)=0
	     range=20
	     If astSize(cyc)>0 Then range=range+10
	     If astSize(v)>0 Then range=range+10
	     If astX#(cyc)>astX#(v)-range And astX#(cyc)<astX#(v)+range And astY#(cyc)>astY#(v)-range And astY#(cyc)<astY#(v)+range
	      ProduceSound(0,sGong,0,Float#(range*2)/100)
	      astA(cyc)=astA(cyc)+180 : astSpeed#(cyc)=Rnd#(1.0,6.0) : astImpactTim(cyc)=10
	      astA(v)=astA(v)+180 : astSpeed#(v)=Rnd#(1.0,6.0) : astImpactTim(v)=10
	     EndIf
	    EndIf
	    ;collide with ship
	    If screenState=1 And astState(v)>0
	     If astSize(v)>0 Then range=28 Else range=16
	     If astShipX#>astX#(v)-range And astShipX#<astX#(v)+range And astShipY#>astY#(v)-range And astShipY#<astY#(v)+range
	      PlaySound sExplode
	      CreateExplosion(astShipX#,astShipY#)
	      PlaySound sFail : screenState=-1 : gotim=0
	     EndIf
	    EndIf
	   Next
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
 ;displays
 DisplayGameStatus(rX#(400),rY#(300))
 DisplayGameChar(gamWorker,rX#(115),rY#(565))
 DisplayScore(gamScore(0),rX#(635),rY#(565))
 ;spaceship
 If screenState<>-1
  For count=0 To 0
		astHeadX#=astShipX#+(Cos(astShipA-90)*(astShipSize-count))
		astHeadY#=astShipY#+(Sin(astShipA-90)*(astShipSize-count))
		astLeftX#=astShipX#+(Cos((astShipA-90)-140)*(astShipSize-count))
		astLeftY#=astShipY#+(Sin((astShipA-90)-140)*(astShipSize-count))
		astRightX#=astShipX#+(Cos((astShipA-90)+140)*(astShipSize-count))
		astRightY#=astShipY#+(Sin((astShipA-90)+140)*(astShipSize-count))
		Color 255,255,255
		Line astHeadX#,astHeadY#,astRightX#,astRightY#
		Line astHeadX#,astHeadY#,astLeftX#,astLeftY#
		Line astRightX#,astRightY#,astLeftX#,astLeftY#
  Next
 EndIf
 ;invaders
 If screenState<>0
  For cyc=1 To no_roids
   If astState(cyc)>0
    If astSize(cyc)=0 Then DrawImage gIconSmall(astType(cyc)),astX#(cyc),astY#(cyc)
    If astSize(cyc)>0 Then DrawImage gIconLarge(astType(cyc)),astX#(cyc),astY#(cyc)
   EndIf
  Next
 EndIf
 ;bullets
 For v=1 To no_bullets
  If bullState(v)>0 Then DrawImage gAstroBullet,bullX(v),bullY(v)
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

;SPAWN BIG ASTEROID
Function SpawnAsteroid()
 cyc=0
 For count=1 To no_roids
  If astState(count)=0 And astSize(count)>0 Then cyc=count : Exit
 Next
 If cyc>0
  PlaySound sInvader
  randy=Rnd(1,4)
  If randy=1 Then astX#(cyc)=Rnd(0,rX#(800)) : astY#(cyc)=0 : astA(cyc)=Rnd(100,260)
  If randy=2 Then astX#(cyc)=Rnd(0,rX#(800)) : astY#(cyc)=rY#(600) : astA(cyc)=Rnd(-80,80)
  If randy=3 Then astX#(cyc)=0 : astY#(cyc)=Rnd(0,rY#(600)) : astA(cyc)=Rnd(10,170)
  If randy=4 Then astX#(cyc)=rX#(800) : astY#(cyc)=Rnd(0,rY#(600)) : astA(cyc)=Rnd(190,350)
  astSpeed#(cyc)=Rnd#(1.0,6.0)
  astState(cyc)=1
 EndIf
End Function

;RELEASE SMALL ASTEROID
Function ReleaseAsteroid(x,y)
 cyc=0
 For count=1 To no_roids
  If astState(count)=0 And astSize(count)=0 Then cyc=count : Exit
 Next
 If cyc>0
  astX#(cyc)=x+Rnd(-20,20) : astY#(cyc)=y+Rnd(-20,20)
  astA(cyc)=Rnd(0,359) : astSpeed#(cyc)=Rnd#(1.0,7.0)
  CreateExplosion(astX#(cyc),astY#(cyc))
  astState(cyc)=1 : astImpactTim(cyc)=10
 EndIf
End Function