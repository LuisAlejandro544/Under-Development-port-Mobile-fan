;//////////////////////////////////////////////////////////////////////////////
;---------------------------- UNDER DEVELOPMENT: WORLD ------------------------
;//////////////////////////////////////////////////////////////////////////////

;DECORATE ARENA
Function DecorateOffice(f)
  ;settings
  ;fedWalls(f)=Rnd(0,8) : fedCeiling(f)=Rnd(0,4) : fedCarpet(f)=Rnd(0,10)  
  ;walls
  tWall=LoadTexture("World/Walls/Wall"+Dig$(fedWalls(f),10)+".JPG")
  EntityTexture FindChild(world,"Walls"),tWall
  FreeTexture tWall
  ;ceiling
  If fedWalls(f)=<1 Or fedWalls(f)=5 Or fedWalls(f)=7 Then fedCeiling(f)=0
  If fedWalls(f)=6 Then fedCeiling(f)=1
  If fedWalls(f)=8 Then fedCeiling(f)=2
  If fedWalls(f)=4 Then fedCeiling(f)=3
  If fedWalls(f)=2 Or fedWalls(f)=3 Then fedCeiling(f)=4
  tCeiling=LoadTexture("World/Ceilings/Ceiling"+Dig$(fedCeiling(f),10)+".JPG")
  EntityTexture FindChild(world,"Ceiling"),tCeiling
  ;If arenaCeiling=1 Or arenaCeiling=2 Then ScaleTexture tCeiling,0.5,0.5
  FreeTexture tCeiling
  ;ground
  tGround=LoadTexture("World/Floors/Ground"+Dig$(fedCarpet(f),10)+".JPG")
  EntityTexture FindChild(world,"Carpet"),tGround
  FreeTexture tGround
  ;windows
  EntityAlpha FindChild(world,"Window"),0.5 
  ;logo
  tempLogo=LoadTexture("Graphics/Companies/Company"+Dig$(f,10)+".png")
  EntityTexture FindChild(world,"Logo"),tempLogo
  FreeTexture tempLogo
  ;posters
  For v=1 To 5
   If FindChild(world,"Poster"+Dig$(v,10))>0
    If deskPoster(v)=0 Then deskPoster(v)=chartGame(Rnd(2,20))
    tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(deskPoster(v),100)+".JPG")
    EntityTexture FindChild(world,"Poster"+Dig$(v,10)),tBoxArt
    FreeTexture tBoxArt
   EndIf
  Next
End Function

;APPLY UNIVERSAL SCENERY
Function PrepareScenery()
 ;plants
 For count=1 To 6
  If FindChild(world,"Plant"+Dig$(count,10))>0
   EntityTexture FindChild(world,"Plant"+Dig$(count,10)),tPlant(deskPlant(6+count))
  EndIf
 Next
 ;luminous signs
 For count=1 To 20
  If FindChild(world,"Sign"+Dig$(count,10))>0
   EntityFX FindChild(world,"Sign"+Dig$(count,10)),1
  EndIf
 Next
 If FindChild(world,"Logo")>0
  EntityFX FindChild(world,"Logo"),1
 EndIf
 ;light bulbs
 For count=1 To 10
  If FindChild(world,"Bulb"+Dig$(count,10))>0
   EntityFX FindChild(world,"Bulb"+Dig$(count,10)),9
   EntityShininess FindChild(world,"Bulb"+Dig$(count,10)),1.0
  EndIf
 Next
End Function

;--------------------------------------------------------------------
;/////////////////////////// ATMOSPHERE /////////////////////////////
;--------------------------------------------------------------------

;LOAD CAMERA
Function LoadCamera() 
 ;camera
 cam=CreateCamera()
 CameraViewport cam,0,0,GraphicsWidth(),GraphicsHeight()
 CameraZoom cam,1.5 
 PositionEntity cam,camX#,camY#,camZ# 
 camAccel=100 : camPivAccel=100
 If screenAgenda<>4 Then camType=10
 ;pivot
 dummy=CreatePivot()
 camPivot=CreatePivot()
End Function

;LOAD LIGHTING
Function LoadLighting() 
 ;lighting
 AmbientLight 220,210,200
 light=CreateLight(1) 
 LightColor light,250,230,210
 ;night time!
 ApplyNightLight()
 ;position
 PositionEntity light,lightX#,lightY#,lightZ#
 lightPivot=CreatePivot()
 PositionEntity lightPivot,lightX#+Rnd(-100,100),lightY#,lightZ#+Rnd(-110,110) 
 PointEntity light,lightPivot
End Function

;APPLY NIGHT LIGHT
Function ApplyNightLight()
 workers=0
 For count=1 To optTeamSize
  If charRoster(gamChar,count)>0
   If charWorked(charRoster(gamChar,count))>0 Or charVacant(charRoster(gamChar,count))>0 Or gamDesk(count)<0 Then workers=workers+1
  EndIf
 Next
 If workers=>TeamSize(gamChar) Then gamNight=1 Else gamNight=0
 If gamNight=1
  EntityColor FindChild(world,"Sky"),50,50,50
  AmbientLight 80,80,100
  LightColor light,100,90,80
  For count=1 To 10
   If FindChild(world,"Bulb"+Dig$(count,10))>0
    EntityColor FindChild(world,"Bulb"+Dig$(count,10)),50,50,50
    EntityFX FindChild(world,"Bulb"+Dig$(count,10)),0
    EntityShininess FindChild(world,"Bulb"+Dig$(count,10)),0
   EndIf
  Next
 EndIf
End Function

;MANAGE ATMOSPHERE
Function ManageAtmos()
 ;randomize colours
 randy=Rnd(0,50)
 If randy=0 Then ambTR#=Rnd(0,255) : ambTG#=Rnd(0,255) : ambTB#=Rnd(0,255)
 If randy=1 Then atmosTR#=Rnd(0,255) : atmosTG#=Rnd(0,255) : atmosTB#=Rnd(0,255) 
 If randy=2 Then lightTR#=Rnd(0,255) : lightTG#=Rnd(0,255) : lightTB#=Rnd(0,255) 
 ;light movement
 randy=Rnd(0,50)
 If (randy=0 And songPlay<>0 And arenaAtmos=<1) Or gotim<0
  PositionEntity light,arenaStageX#+Rnd(-100,100),arenaStageY#+75,arenaStageZ#+Rnd(-100,100)
  PositionEntity lightPivot,arenaStageX#+Rnd(-100,100),arenaStageY#,arenaStageZ#+Rnd(-100,100)
  PointEntity light,lightPivot
 EndIf
 ;normal override
 If songPlay=0 Or arenaAtmos=2
  ambTR#=220 : ambTG#=210 : ambTB#=200
  atmosTR#=160 : atmosTG#=130 : atmosTB#=100;atmosTR#=200 : atmosTG#=200 : atmosTB#=240
  lightTR#=250 : lightTG#=230 : lightTB#=150 
 EndIf
 If arenaAtmos=1 Then atmosTR#=160 : atmosTG#=130 : atmosTB#=100
 ;apply ambience
 changer#=5.0
 If gotim=<100 Then changer#=PercentOf#(changer#,gotim)
 If gotim<0 Then changer#=0
 If ambR#<ambTR# Then ambR#=ambR#+changer#
 If ambR#>ambTR# Then ambR#=ambR#-changer#
 If ambG#<ambTG# Then ambG#=ambG#+changer#
 If ambG#>ambTG# Then ambG#=ambG#-changer#
 If ambB#<ambTB# Then ambB#=ambB#+changer#
 If ambB#>ambTB# Then ambB#=ambB#-changer# 
 AmbientLight ambR#,ambG#,ambB#
 ;apply atmosphere
 If atmosR#<atmosTR# Then atmosR#=atmosR#+changer#
 If atmosR#>atmosTR# Then atmosR#=atmosR#-changer#
 If atmosG#<atmosTG# Then atmosG#=atmosG#+changer#
 If atmosG#>atmosTG# Then atmosG#=atmosG#-changer#
 If atmosB#<atmosTB# Then atmosB#=atmosB#+changer#
 If atmosB#>atmosTB# Then atmosB#=atmosB#-changer# 
 CameraFogColor cam,atmosR#,atmosG#,atmosB#
 ;apply lighting
 If lightR#<lightTR# Then lightR#=lightR#+changer#
 If lightR#>lightTR# Then lightR#=lightR#-changer#
 If lightG#<lightTG# Then lightG#=lightG#+changer#
 If lightG#>lightTG# Then lightG#=lightG#-changer#
 If lightB#<lightTB# Then lightB#=lightB#+changer#
 If lightB#>lightTB# Then lightB#=lightB#-changer# 
 LightColor light,lightR#,lightG#,lightB#
End Function

;--------------------------------------------------------------------
;//////////////////// CAMERA OPERATIONS /////////////////////////////
;--------------------------------------------------------------------
Function Camera()
 ;store old position
 camOldX#=camX# : camOldY#=camY# : camOldZ#=camZ#
 ;ESTABLISH FOCUS
 ;focus on character
 If camFoc>0
  If pChar(camFoc)>0
   camPivTX#=pX#(camFoc) : camPivTY#=EyeLevel#(camFoc) : camPivTZ#=pZ#(camFoc) 
   If pAnim(camFoc)=11 Then camPivTX#=EntityX(pLimb(camFoc,1),1) : camPivTY#=camPivTY#-2 : camPivTZ#=EntityZ(pLimb(camFoc,1),1)
  Else
   If screen=50
    camPivTX#=deskX#(camFoc) : camPivTY#=deskY#(camFoc)+23 : camPivTZ#=deskZ#(camFoc)
   EndIf
  EndIf
 EndIf
 ;focus on screen
 screener=0
 If screen=50 And screenAgenda=0 Then screener=1
 If screen=50 And screenAgenda=2 And screenState=2 Then screener=1
 If screen=50 And screenAgenda=4 Then screener=1
 If screen=50 And screenAgenda=6 And screenState=>0 And camFoc=negStar Then screener=1
 If screen=50 And screenAgenda=8 Then screener=1
 If screen=53 And camType=2 And desk(camFoc)>0 Then screener=1
 If screener>0
  screenFoc=FindChild(desk(camFoc),hardTV$(deskHardware(camFoc))+"S")
  If FindChild(desk(camFoc),hardCom$(deskHardware(camFoc))+"S")>0 And screenAgenda>0 And screenAgenda<>6
   screenFoc=FindChild(desk(camFoc),hardCom$(deskHardware(camFoc))+"S") 
  EndIf
  If screen=50 And screenAgenda=8 Then screenFoc=FindChild(desk(camFoc),hardModel$(deskHardware(camFoc)))
  If (pAnim(camFoc)=>16 And pAnim(camFoc)=<18) Or (pAnim(camFoc)=>26 And pAnim(camFoc)=<28)
   If FindChild(p(camFoc),hardModel$(deskHardware(camFoc))+"S")>0
    screenFoc=FindChild(p(camFoc),hardModel$(deskHardware(camFoc))+"S") 
   EndIf
   If screenAgenda=8 Then screenFoc=FindChild(p(camFoc),hardModel$(deskHardware(camFoc)))
  EndIf
  If screenAgenda=6 And (pAnim(camFoc)=15 Or pAnim(camFoc)=25) Then screenFoc=FindChild(p(camFoc),"Box")
  camPivTX#=EntityX(screenFoc,1) : camPivTY#=EntityY(screenFoc,1) : camPivTZ#=EntityZ(screenFoc,1)
 EndIf
 ;GET TARGET POSITION
 ;manual control
 If screen=50 And (screenAgenda=0 Or screenAgenda=8) And gotim>100
  camMouseX#=MouseXSpeed()/2
  camMouseY#=-(MouseYSpeed()/2)
  If camMouseX#<>0 Or camMouseY#<>0 Then camType=0 : camTim=100
  MoveMouse rX#(400),rY#(300)
 Else
  void#=MouseXSpeed()
  void#=MouseYSpeed()
 EndIf
 If camType=0
  If MouseDown(1) Or MouseDown(2)
   If camMouseY#>0 And ReachedCord(camX#,camZ#,camPivTX#,camPivTZ#,5) And camY#=>camPivTY#-5 And camY#=<camPivTY#+5
    MoveEntity cam,camMouseX#,0,0
   Else
    MoveEntity cam,camMouseX#,0,camMouseY#
   EndIf
  Else
   MoveEntity cam,camMouseX#,camMouseY#/2,0
  EndIf
  camX#=EntityX(cam) : camTX#=camX#
  camY#=EntityY(cam) : camTY#=camY#
  camZ#=EntityZ(cam) : camTZ#=camZ#
 EndIf 
 ;static in centre of office
 If camType=1
  camTX#=0 : camTY#=wGround#+40 : camTZ#=0
  If screenAgenda=2 And screenState=0
   PositionEntity dummy,deskX#(camFoc),wGround#+25,deskZ#(camFoc)
   RotateEntity dummy,0,deskA#(camFoc),0
   MoveEntity dummy,40,0,0
   camPivTX#=EntityX(dummy) : camPivTY#=EntityY(dummy) : camPivTZ#=EntityZ(dummy)  
  EndIf
 EndIf
 ;frame screen/object
 If camType=2
  nearestSide=NearestSideTo(camFoc,camX#,camZ#)
  PositionEntity dummy,camPivTX#,camPivTY#,camPivTZ#
  RotateEntity dummy,0,deskA#(camFoc),0
  zoom#=-20 : swing#=12 : elevation#=5
  If screenAgenda=4 Then zoom#=-25 : swing#=15 : elevation#=5
  If screenAgenda=8 Then zoom#=-8 : swing#=-6 : elevation#=5
  If (pAnim(camFoc)=>16 And pAnim(camFoc)=<18) Or (pAnim(camFoc)=>26 And pAnim(camFoc)=<28) Then zoom#=-8 : swing#=5 : elevation#=2
  If screenAgenda=6 And (pAnim(camFoc)=15 Or pAnim(camFoc)=25) Then zoom#=-10 : swing#=7 : elevation#=5
  ;If nearestSide=1 And swing#>0 Then swing#=-swing#
  MoveEntity dummy,swing#,elevation#,zoom#
  camTX#=EntityX(dummy) : camTY#=EntityY(dummy) : camTZ#=EntityZ(dummy)  
 EndIf
 ;zoom into screen
 If camType=3
  camTX#=camPivTX# : camTY#=camPivTY# : camTZ#=camPivTZ#
 EndIf
 ;retreat from pivot
 If camType=4
  PositionEntity dummy,camX#,camY#,camZ#
  PointEntity dummy,camPivot
  RotateEntity dummy,0,EntityYaw(dummy),0
  MoveEntity dummy,0,2,-10
  camTX#=EntityX(dummy) : camTY#=EntityY(dummy) : camTZ#=EntityZ(dummy) 
 EndIf
 ;cut-scene head shot
 If camType=10 And camFoc>0
  nearestSide=NearestSideTo(camFoc,camX#,camZ#)
  ;nearestDepth=NearestDepthTo(camFoc,camX#,camZ#)
  PositionEntity dummy,camPivTX#,camPivTY#,camPivTZ#
  RotateEntity dummy,0,pA#(camFoc),0
  zoom#=35 : swing#=0 : elevation#=5
  ;If nearestDepth=2 And screen=53 And negSetting=>6 And negSetting=<7 Then zoom#=-20 
  If nearestSide=1 Then swing#=-15 Else swing#=15
  If (screen=>52 And screen=<53) Or (screen=53 And negSetting=6)
   If camFoc=negVisitor Then swing#=-15 Else swing#=15
  EndIf
  If screen=54 Or (screen=53 And negSetting=7)
   If camFoc=negStar Then swing#=-15 Else swing#=15
  EndIf
  If negSetting=>6 And negSetting=<7 zoom#=20
  If screen=50 And screenAgenda=6 And pChar(camFoc)=fedBooker(charFed(gamChar)) Then zoom#=30 : swing#=0 : elevation#=5
  If pAnim(camFoc)=11 Then zoom#=5 : swing#=-20 : elevation#=8
  MoveEntity dummy,swing#,elevation#,zoom#
  camTX#=EntityX(dummy) : camTY#=EntityY(dummy) : camTZ#=EntityZ(dummy)  
 EndIf
 ;PURSUE TARGETS
 ;target limits
 If camTX#<wWest# Then camTX#=wWest#
 If camTX#>wEast# Then camTX#=wEast#
 If camTY#<wGround# Then camTY#=wGround#
 If camTY#>wCeiling# Then camTY#=wCeiling# 
 If camTZ#<wSouth# Then camTZ#=wSouth#
 If camTZ#>wNorth# Then camTZ#=wNorth# 
 ;camera tracking 
 If gotim=>0
  speeder=60
  GetSmoothSpeeds(camX#,camTX#,camY#,camTY#,camZ#,camTZ#,speeder)
  camAccel=camAccel+5
  If camAccel>100 Then camAccel=100
  speedX#=PercentOf#(speedX#,camAccel)
  speedY#=PercentOf#(speedY#,camAccel)
  speedZ#=PercentOf#(speedZ#,camAccel)
  If camX#<camTX# Then camX#=camX#+speedX#
  If camX#>camTX# Then camX#=camX#-speedX#
  If camY#<camTY# Then camY#=camY#+speedY#
  If camY#>camTY# Then camY#=camY#-speedY#
  If camZ#<camTZ# Then camZ#=camZ#+speedZ#
  If camZ#>camTZ# Then camZ#=camZ#-speedZ#
 EndIf
 ;offset position
 ;veer=0
 ;If screen=50 And screenAgenda=2 And screenState=2 And gotim>0 And gotim<50
  ;If FindChild(desk(camFoc),hardCom$(deskHardware(camFoc))+"S")>0 Then veer=1
 ;EndIf
 ;If screen=50 And screenAgenda=4 And screenState=0 And gotim>0 And gotim<50
  ;If (pAnim(camFoc)=>16 And pAnim(camFoc)=<18) Or (pAnim(camFoc)=>26 And pAnim(camFoc)=<28)
   ;If FindChild(desk(camFoc),hardCom$(deskHardware(camFoc))+"S")>0 Then veer=-1
  ;EndIf
 ;EndIf
 ;If veer<>0
  ;factor#=100
  ;If gotim=<20 Then factor#=gotim*5
  ;If gotim=>30 And gotim=<50 Then factor#=(50-gotim)*5
  ;PositionEntity dummy,camX#,camY#,camZ#
  ;RotateEntity dummy,0,EntityYaw(cam),0
  ;MoveEntity dummy,PercentOf#(veer,factor#),0,0
  ;camX#=EntityX(dummy) : camY#=EntityY(dummy) : camZ#=EntityZ(dummy)  
 ;EndIf
 ;limits
 If camX#<wWest# Then camX#=wWest#
 If camX#>wEast# Then camX#=wEast#
 If camY#<wGround#+2 Then camY#=wGround#+2
 If camY#>wCeiling# Then camY#=wCeiling# 
 If camZ#<wSouth# Then camZ#=wSouth#
 If camZ#>wNorth# Then camZ#=wNorth# 
 ;pivot tracking 
 speeder=30
 If screen=50 And screenAgenda=2 And screenState=1 Then speeder=15
 GetSmoothSpeeds(camPivX#,camPivTX#,camPivY#,camPivTY#,camPivZ#,camPivTZ#,speeder)
 camPivAccel=camPivAccel+5
 If camPivAccel>100 Then camPivAccel=100
 speedX#=PercentOf#(speedX#,camPivAccel)
 speedY#=PercentOf#(speedY#,camPivAccel)
 speedZ#=PercentOf#(speedZ#,camPivAccel)
 If camPivX#<camPivTX# Then camPivX#=camPivX#+speedX#
 If camPivX#>camPivTX# Then camPivX#=camPivX#-speedX#
 If camPivY#<camPivTY# Then camPivY#=camPivY#+speedY#
 If camPivY#>camPivTY# Then camPivY#=camPivY#-speedY#
 If camPivZ#<camPivTZ# Then camPivZ#=camPivZ#+speedZ#
 If camPivZ#>camPivTZ# Then camPivZ#=camPivZ#-speedZ#
 If screenAgenda=4 Then camPivX#=camPivTX# : camPivY#=camPivTY# : camPivZ#=camPivTZ#
 ;position & point 
 PositionEntity cam,camX#,camY#,camZ#
 PositionEntity camPivot,camPivX#,camPivY#,camPivZ#
 PointEntity cam,camPivot
End Function

;GET SUITABLE HEIGHT FOR FOCUSING
Function EyeLevel#(cyc)
 ;standing height
 height#=pY#(cyc)+(25+(charHeight(pChar(cyc))/3))
 ;seated height
 If screen=50 Or (screen=>52 And screen=<54)
  If pAnim(cyc)<20 Then height#=pY#(cyc)+23
 EndIf
 Return height#
End Function

;NEAREST SIDE TO ACTOR
Function NearestSideTo(cyc,x#,z#) ;1=left, 2=right 
 ;find distance to left
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 MoveEntity dummy,-20,0,0
 leftX#=EntityX(dummy) : leftZ#=EntityZ(dummy)
 ;find distance to right
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 MoveEntity dummy,20,0,0
 rightX#=EntityX(dummy) : rightZ#=EntityZ(dummy)
 ;return closest
 If GetDistance#(x#,z#,leftX#,leftZ#)<GetDistance#(x#,z#,rightX#,rightZ#) Then value=1 Else value=2
 Return value
End Function

;NEAREST DEPTH TO ACTOR
Function NearestDepthTo(cyc,x#,z#) ;1=front, 2=behind
 ;find distance in front
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 MoveEntity dummy,0,0,30
 frontX#=EntityX(dummy) : front#=EntityZ(dummy)
 ;find distance behind
 PositionEntity dummy,pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity dummy,0,pA#(cyc),0
 MoveEntity dummy,0,0,-30
 behindX#=EntityX(dummy) : behindZ#=EntityZ(dummy)
 ;return closest
 If GetDistance#(x#,z#,frontX#,frontZ#)<GetDistance#(x#,z#,behindX#,behindZ#) Then value=1 Else value=2
 Return value
End Function