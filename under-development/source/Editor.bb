;//////////////////////////////////////////////////////////////////////////////
;------------------------- UNDER DEVELOPMENT: EDITOR --------------------------
;//////////////////////////////////////////////////////////////////////////////

;-------------------------------------------------------------------
;///////////////////// 51. EDIT CHARACTER //////////////////////////
;-------------------------------------------------------------------
Function Editor()
;loading
Loader("Please Wait","Loading Editor")
ResetTextures()
;camera
cam=CreateCamera()
CameraViewport cam,0,0,GraphicsWidth(),GraphicsHeight()
CameraZoom cam,1.5
CameraClsMode cam,0,1
camX#=-219 : camY#=35 : camZ#=-83
camXA#=3 : camYA#=119 : camZA#=0
If GraphicsWidth()>1024 Then camX#=-212 : camY#=35 : camZ#=-79
PositionEntity cam,camX#,camY#,camZ#
RotateEntity cam,camXA#,camYA#,camZA#
;atmosphere
AmbientLight 200,190,170
light=CreateLight(3)
PositionEntity light,-145,100,-70
RotateEntity light,90,0,0
LightRange light,500
LightConeAngles light,0,135
LightColor light,220,200,180
;load model
If editChar=<no_chars Then CopyChar(editChar,0) : charFed(0)=charFed(editChar)
If editChar=no_chars+1 Then CopyChar(1,0) : charFed(editChar)=fed : charFed(0)=charFed(editChar)
cyc=1 : pChar(cyc)=0
pX#(cyc)=-257 : pY#(cyc)=11.5 : pZ#(cyc)=-124 
oldState=-1
LoadMuse(cyc)
LoadShadows(cyc)
PointEntity light,p(cyc) 
;assess blocks
blockData=0 : blockName=0 : blockDemeanour=0
blockHair=0 : blockCostume=0 : blockContract=0
If game=1
 blockContract=1
 If screenAgenda<>5 Then blockData=1 
 If editChar<>gamChar Then blockName=1
EndIf
gamShopping=0
;page properties
no_pages=4
pageList(1)=8
pageList(2)=10
pageList(3)=12
pageList(4)=12
page=1
If game=1 Then page=4
If game=1 And screenAgenda=5 Then page=2
foc=pageList(page) : oldfoc=foc
editExamine=0
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
go=0 : gotim=0 : keytim=10
While go=0
	
 Cls
 screenCall=0
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0
    flashTim=flashTim+1
	If flashTim>30 Then flashTim=0  
	
	;PORTAL 
	gotim=gotim+1
	If gotim>20 And keytim=0
	 ;quit
	 If KeyDown(1) Then go=-1
	 ;activations
	 If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
	  ;next page
	  If foc=pageList(page)-2 And keytim=0
	   PlaySound sMenuBrowse : PlaySound sMenuBrowse : gotim=10 : keytim=10
	   page=page+1
	   If page>4 Then page=1
	   foc=pageList(page)-2 : oldfoc=foc
	  EndIf
	  ;previous page
	  If foc=pageList(page)-1 And keytim=0
	   PlaySound sMenuBrowse : PlaySound sMenuBrowse : gotim=10 : keytim=10
	   page=page-1
	   If page<1 Then page=4
	   foc=pageList(page)-1 : oldfoc=foc
	  EndIf
	  ;leave
	  If foc=pageList(page) And keytim=0 Then go=1
	  ;detour to relationships
	  If page=1 And foc=5
	   go=2
	   If game=1 Then BlockAccess()
	  EndIf
	 EndIf
	EndIf
	
	;CAREER POINTS
	If game=1 And screenAgenda=5 Then editPoints=300 Else editPoints=999
	editPoints=editPoints-charDesign(0)
	editPoints=editPoints-charProgram(0)
	editPoints=editPoints-char2D(0) 
	editPoints=editPoints-char3D(0)
	editPoints=editPoints-charSound(0) 
	editPoints=editPoints-charMusic(0)       
	If editPoints<0 Then editPoints=0
	
	;1. PROFILE
	If page=1 And keytim=0
	 ;search left
	 If MouseDown(2) And MouseX()>rX#(400) And editExamine=0
	  If foc=2 And blockContract=0 Then charFed(0)=charFed(0)-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=3 And blockContract=0 Then charContract(0)=charContract(0)-1 : PlaySound sMenuBrowse : keytim=3
	  range=FigureRange(negSalary)
	  If range<100 Then range=100
	  If foc=4 And blockContract=0 Then charSalary(0)=charSalary(0)-range : PlaySound sMenuBrowse : keytim=2
	 EndIf
	 ;search right
	 If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
	  If foc=2 And blockContract=0 Then charFed(0)=charFed(0)+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=3 And blockContract=0 Then charContract(0)=charContract(0)+1 : PlaySound sMenuBrowse : keytim=3
	  range=FigureRange(negSalary)
	  If range<100 Then range=100
	  If foc=4 And blockContract=0 Then charSalary(0)=charSalary(0)+range : PlaySound sMenuBrowse : keytim=2
	 EndIf 
	 ;randomize
	 If KeyDown(57)
	  If foc=2 And blockContract=0 Then charFed(0)=Rnd(1,no_feds) : PlaySound sMenuBrowse : keytim=5
	  If foc=3 And blockContract=0 Then charContract(0)=Rnd(1,48) : PlaySound sMenuBrowse : keytim=5
	  If foc=4 And blockContract=0 Then charSalary(0)=RoundOff(Rnd(100,10000),100) : PlaySound sMenuBrowse : keytim=5
	 EndIf 
	 ;revert
	 If KeyDown(14)
	  If foc=2 And blockContract=0 Then charFed(0)=fed : PlaySound sMenuBrowse : keytim=10
	  If foc=3 And blockContract=0 Then charContract(0)=0 : PlaySound sMenuBrowse : keytim=10
	  If foc=4 And blockContract=0 Then charSalary(0)=CalculateWorth(0) : PlaySound sMenuBrowse : keytim=10
	 EndIf 
	 ;reset relationships
	 If foc=5 And blockData=0 And screenAgenda<>5
	  If KeyDown(14) Or KeyDown(211)
	   PlaySound sTrash : keytim=10
	   For v=1 To no_chars
	    charRelationship(editChar,v)=0
	    charRelationship(v,editChar)=0
	   Next
	  EndIf
	  ;generate relationships
	  If KeyDown(57)
	   PlaySound sSwing : keytim=10
	   For v=1 To no_chars
	    charRelationship(editChar,v)=0
	    charRelationship(v,editChar)=0
	   Next
	   For v=1 To no_chars
	    If v<>editChar
	     randy=Rnd(0,20)
	     If randy=<1 Then charRelationship(editChar,v)=Rnd(-1,1)
	     randy=Rnd(0,1)
	     If randy=0 And charTeam(editChar)>0 And charTeam(editChar)=charTeam(v) Then charRelationship(editChar,v)=1
	     charRelationship(v,editChar)=charRelationship(editChar,v)
	    EndIf
	   Next
	  EndIf  
	 EndIf
	EndIf  
	
	;2. ATTRIBUTES
	If page=2 And keytim=0 And blockData=0
	 ;search left
	 If MouseDown(2) And MouseX()>rX#(400) And editExamine=0 
	  If foc=1 Then charDesign(0)=charDesign(0)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=2 Then charProgram(0)=charProgram(0)-1 : PlaySound sMenuBrowse : keytim=2
      If foc=3 Then char2D(0)=char2D(0)-1 : PlaySound sMenuBrowse : keytim=2
      If foc=4 Then char3D(0)=char3D(0)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=5 Then charSound(0)=charSound(0)-1 : PlaySound sMenuBrowse : keytim=2
      If foc=6 Then charMusic(0)=charMusic(0)-1 : PlaySound sMenuBrowse : keytim=2
      If foc=7 And screenAgenda<>5 Then charRep(0)=charRep(0)-1 : PlaySound sMenuBrowse : keytim=2   
	 EndIf
	 ;search right
	 If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
	  If editPoints>0
	   If foc=1 Then charDesign(0)=charDesign(0)+1 : PlaySound sMenuBrowse : keytim=2
	   If foc=2 Then charProgram(0)=charProgram(0)+1 : PlaySound sMenuBrowse : keytim=2
       If foc=3 Then char2D(0)=char2D(0)+1 : PlaySound sMenuBrowse : keytim=2
       If foc=4 Then char3D(0)=char3D(0)+1 : PlaySound sMenuBrowse : keytim=2
	   If foc=5 Then charSound(0)=charSound(0)+1 : PlaySound sMenuBrowse : keytim=2
       If foc=6 Then charMusic(0)=charMusic(0)+1 : PlaySound sMenuBrowse : keytim=2
       If foc=7 And screenAgenda<>5 Then charRep(0)=charRep(0)+1 : PlaySound sMenuBrowse : keytim=2 
	  EndIf
	 EndIf 
	 ;randomize
	 If KeyDown(57)
	  PlaySound sMenuBrowse : keytim=5
	  If foc=1 Then charDesign(0)=Rnd(25,99)
	  If foc=2 Then charProgram(0)=Rnd(25,99)
      If foc=3 Then char2D(0)=Rnd(25,99)
      If foc=4 Then char3D(0)=Rnd(25,99)
      If foc=5 Then charSound(0)=Rnd(25,99)
      If foc=6 Then charMusic(0)=Rnd(25,99)
      If foc=7 And screenAgenda<>5 Then charRep(0)=Rnd(25,99)
	 EndIf 
	 ;revert
	 If KeyDown(14)
	  PlaySound sMenuBrowse : keytim=10
	  If foc=1 Then charDesign(0)=25
	  If foc=2 Then charProgram(0)=25
      If foc=3 Then char2D(0)=25
      If foc=4 Then char3D(0)=25
      If foc=5 Then charSound(0)=25
      If foc=6 Then charMusic(0)=25
      If foc=7 And screenAgenda<>5 Then charRep(0)=25
	 EndIf  
	EndIf
	
	;3. BUILD
	oldHeadwear=charHatStyle(0)  
	If page=3 And keytim=0
	 ;search left
	 If MouseDown(2) And MouseX()>rX#(400) And editExamine=0
	  If foc=1 And blockData=0 Then charAge(0)=charAge(0)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=2 And blockData=0 Then charHeight(0)=charHeight(0)-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=3 And blockData=0 Then charBuild(0)=charBuild(0)-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=4 And blockDemeanour=0 Then charEyes(0)=charEyes(0)-1 : PlaySound sMenuBrowse : keytim=8
	  If foc=5 And blockCostume=0 Then charHatStyle(0)=charHatStyle(0)-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=6 And blockCostume=0 Then charHat(0)=charHat(0)-1 : PlaySound sMenuBrowse : keytim=5
	  If foc=7 And blockCostume=0 Then charSpecs(0)=charSpecs(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=8 And blockCostume=0 Then charAccessory(0)=charAccessory(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=9 And blockCostume=0 Then charBaggy(0)=charBaggy(0)-1 : PlaySound sMenuBrowse : keytim=8
	 EndIf
	 ;search right
	 If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
	  If foc=1 And blockData=0 Then charAge(0)=charAge(0)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=2 And blockData=0 Then charHeight(0)=charHeight(0)+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=3 And blockData=0 Then charBuild(0)=charBuild(0)+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=4 And blockDemeanour=0 Then charEyes(0)=charEyes(0)+1 : PlaySound sMenuBrowse : keytim=8
	  If foc=5 And blockCostume=0 Then charHatStyle(0)=charHatStyle(0)+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=6 And blockCostume=0 Then charHat(0)=charHat(0)+1 : PlaySound sMenuBrowse : keytim=5
	  If foc=7 And blockCostume=0 Then charSpecs(0)=charSpecs(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=8 And blockCostume=0 Then charAccessory(0)=charAccessory(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=9 And blockCostume=0 Then charBaggy(0)=charBaggy(0)+1 : PlaySound sMenuBrowse : keytim=8
	 EndIf 
	 ;randomize
	 If KeyDown(57)
	  PlaySound sMenuBrowse : keytim=5
	  If foc=1 And blockData=0 Then charAge(0)=Rnd(16,60)
	  If foc=2 And blockData=0 Then charHeight(0)=Rnd(0,24)
	  If foc=3 And blockData=0 Then charBuild(0)=Rnd(1,no_models)
	  If foc=4 And blockDemeanour=0 Then charEyes(0)=Rnd(1,3)
	  If foc=5 And blockCostume=0 Then charHatStyle(0)=Rnd(0,no_hatstyles)
	  If foc=6 And blockCostume=0 Then charHat(0)=Rnd(1,no_hats)
	  If foc=7 And blockCostume=0 Then charSpecs(0)=Rnd(1,no_specs)
      If foc=8 And blockCostume=0 Then charAccessory(0)=Rnd(1,no_accessories)
      If foc=9 And blockCostume=0 Then charBaggy(0)=Rnd(0,3)
	 EndIf  
	 ;revert
	 If KeyDown(14)
	  PlaySound sMenuBrowse : keytim=10 
	  If foc=1 And blockData=0 Then charAge(0)=18
	  If foc=2 And blockData=0 Then charHeight(0)=12
	  If foc=3 And blockData=0 Then charBuild(0)=2
	  If foc=4 And blockDemeanour=0 Then charEyes(0)=2
	  If foc=5 And blockCostume=0 Then charHatStyle(0)=0
	  If foc=6 And blockCostume=0 Then charHat(0)=1
	  If foc=7 And blockCostume=0 Then charSpecs(0)=0
      If foc=8 And blockCostume=0 Then charAccessory(0)=0
      If foc=9 And blockCostume=0 Then charBaggy(0)=0
	 EndIf 
	 ;refresh model
	 If KeyDown(28) And foc=3 And charBuild(0)<>editModel
	  PlaySound sMenuGo : keytim=10
	  screenCall=1
	 EndIf    
	EndIf 
	
	;4. CLOTHING
	If page=4 And keytim=0
	 ;search left
	 If MouseDown(2) And MouseX()>rX#(400) And editExamine=0
	  If foc=1 And blockHair=0 Then charHairStyle(0)=charHairStyle(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=2 And blockHair=0 Then charHair(0)=charHair(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=3 And blockData=0 Then charFace(0)=charFace(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=4 And blockCostume=0 Then charBeard(0)=charBeard(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=5 And blockCostume=0 Then charBody(0)=charBody(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=6 And blockCostume=0 Then charArms(0)=charArms(0)-1 : PlaySound sMenuBrowse : keytim=5
      If foc=7 And blockCostume=0
       PlaySound sMenuBrowse : keytim=5
       Repeat
        charShorts(0)=charShorts(0)-1
        If charShorts(0)<0 Then charShorts(0)=no_legs
       Until ShortsViable(charShorts(0))
      EndIf
      If foc=8 And blockCostume=0
       PlaySound sMenuBrowse : keytim=5
       charLegs(0)=charLegs(0)-1
       If charLegs(0)<1 Then charLegs(0)=no_legs
       charShoes(0)=charLegs(0)
      EndIf
      If foc=9 And blockCostume=0 Then charShoes(0)=charShoes(0)-1 : PlaySound sMenuBrowse : keytim=5
	 EndIf
	 ;search right
	 If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
	  If foc=1 And blockHair=0 Then charHairStyle(0)=charHairStyle(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=2 And blockHair=0 Then charHair(0)=charHair(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=3 And blockData=0 Then charFace(0)=charFace(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=4 And blockCostume=0 Then charBeard(0)=charBeard(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=5 And blockCostume=0 Then charBody(0)=charBody(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=6 And blockCostume=0 Then charArms(0)=charArms(0)+1 : PlaySound sMenuBrowse : keytim=5
      If foc=7 And blockCostume=0
       PlaySound sMenuBrowse : keytim=5
       Repeat
        charShorts(0)=charShorts(0)+1
        If charShorts(0)>no_legs Then charShorts(0)=0
       Until ShortsViable(charShorts(0))
      EndIf
      If foc=8 And blockCostume=0
       PlaySound sMenuBrowse : keytim=5
       charLegs(0)=charLegs(0)+1
       If charLegs(0)>no_legs Then charLegs(0)=1
       charShoes(0)=charLegs(0)
      EndIf
      If foc=9 And blockCostume=0 Then charShoes(0)=charShoes(0)+1 : PlaySound sMenuBrowse : keytim=5
	 EndIf 
	 ;randomize
	 If KeyDown(57)
	  PlaySound sMenuBrowse : keytim=5
	  If foc=1 And blockHair=0 Then charHairStyle(0)=Rnd(1,no_hairstyles)
      If foc=2 And blockHair=0 Then charHair(0)=Rnd(1,no_hairs)
      If foc=3 And blockData=0 Then charFace(0)=Rnd(1,no_faces)
      If foc=4 And blockCostume=0 Then charBeard(0)=Rnd(1,no_beards)
      If foc=5 And blockCostume=0 Then charBody(0)=Rnd(1,no_bodies)
      If foc=6 And blockCostume=0 Then charArms(0)=Rnd(1,no_arms)
      If foc=7 And blockCostume=0
       Repeat
        charShorts(0)=Rnd(0,no_legs)
       Until ShortsViable(charShorts(0))
      EndIf
      If foc=8 And blockCostume=0 Then charLegs(0)=Rnd(1,no_legs) : charShoes(0)=charLegs(0)
      If foc=9 And blockCostume=0 Then charShoes(0)=Rnd(1,no_legs)
	 EndIf  
	 ;revert
	 If KeyDown(14)
	  PlaySound sMenuBrowse : keytim=10 
	  If foc=1 And blockHair=0 Then charHairStyle(0)=0
      If foc=2 And blockHair=0 Then charHair(0)=1
      If foc=3 And blockData=0 Then charFace(0)=1
      If foc=4 And blockCostume=0 Then charBeard(0)=0
      If foc=5 And blockCostume=0 Then charBody(0)=1
      If foc=6 And blockCostume=0 Then charArms(0)=1
      If foc=7 And blockCostume=0 Then charShorts(0)=0
      If foc=8 And blockCostume=0 Then charLegs(0)=1 : charShoes(0)=charLegs(0)
      If foc=9 And blockCostume=0 Then charShoes(0)=charLegs(0)  
	 EndIf    
	EndIf 
	;hands
	If GetGender(0)=1 Then charHands(0)=2 Else charHands(0)=1
	If charArms(0)=4 Then charHands(0)=5
	If charArms(0)=5 Or charArms(0)=7 Then charHands(0)=6
	If charArms(0)=6 Then charHands(0)=10 
	If charArms(0)=17 Then charHands(0)=8
	If charArms(0)=3 Then charHands(0)=11     
	;If KeyDown(51) And keytim=0 Then charHands(0)=charHands(0)-1 : keytim=10 
	;If KeyDown(52) And keytim=0 Then charHands(0)=charHands(0)+1 : keytim=10       
	
    ;UPDATE MODEL
	;check limits
	CheckLimits(0) 
	;switch to ideal headwear
	If charHatStyle(0)<>oldHeadwear
	 If charHatStyle(0)=1 Or charHatStyle(0)=10
	  If charHat(0)<13 Then charHat(0)=13
	 Else 
	  If charHat(0)=>13 Then charHat(0)=1
	 EndIf
	EndIf
	;update costume
	ApplyCostume(cyc) 
	EntityTexture FindChild(p(cyc),"Head"),tEyes(charEyes(0)),0,3 
	;charge for changes
	gamShopping=FindCostumeChanges()  
	;scale
	scaler#=0.055+(GetPercent#(charHeight(0),24)/10000)
    ScaleEntity p(cyc),scaler#,scaler#,scaler# 
    ;orientation
    If MouseX()>rX#(120) And MouseX()<rX#(300) And MouseY()>rY#(100) And MouseY()<rY#(550)
     If (MouseDown(1) Or MouseDown(2)) And editExamine=0 Then PlaySound sMenuSelect : editExamine=1 : void=MouseXSpeed()
    EndIf
    If MouseDown(1)=0 And MouseDown(2)=0 And editExamine=1 Then PlaySound sMenuSelect : editExamine=0
    If editExamine=1
     TurnEntity p(cyc),0,MouseXSpeed()+360,0
    Else
     RotateEntity p(cyc),0,345,0
    EndIf
	;shadows
	For limb=1 To 50
     If pShadow(cyc,limb)>0
      RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
      PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
      If go>0 Then EntityAlpha pShadow(cyc,limb),0
     EndIf
    Next 

    ;ANIMATIONS
    ;get required state
    oldState=pState(cyc)
    randy=Rnd(0,1000)
    If randy=<1 Then pState(cyc)=Rnd(2,5)
    If GetGender(0)=1 Then pState(cyc)=1
    ;trigger new stance
    If pState(cyc)<>oldState Then Animate p(cyc),1,Rnd#(0.2,0.4),pSeq(cyc,pState(cyc)),5
    ;manage music
	ManageMusic(0)

    ;prepare to take photo
    If go>0
     CameraClsColor cam,25,5,5
     CameraClsMode cam,1,1
    EndIf

 UpdateWorld
 Next
 ;underlying images
 If go<>1
  DrawImage gBackground,rX#(400),rY#(300)
  DrawImage gLabel(charFed(0)),rX#(400),rY#(60)
 EndIf
 RenderWorld 1

 ;DISPLAY
 foc=0
 advice$="" : adviceX=0 : adviceY=0
 warning$="" : warnX=0 : warnY=0 
 ;1. PROFILE
 If page=1
  ;main options
  x=575 : y=180
  DrawOption(1,rX#(x),rY#(y),"Name",charName$(0)) : y=y+45
  DrawOption(2,rX#(x),rY#(y),"Company",fedName$(charFed(0))) : y=y+40
  DrawOption(3,rX#(x),rY#(y),"Contract",charContract(0)+" "+textPeriod$(optTime)+"s") : y=y+40
  DrawOption(4,rX#(x),rY#(y),"Salary","$"+GetFigure$(charSalary(0)))
  y=y+60
  DrawOption(5,rX#(x),rY#(y),"RELATIONSHIPS","")
  DrawOption(6,rX#(x),rY#(470),">>> ATTRIBUTES >>>","")
  DrawOption(7,rX#(x),rY#(506),"<<< CLOTHING <<<","")
  DrawOption(8,rX#(x),rY#(545),"SAVE & EXIT","")
  ;enter names
  y=180
  If MouseDown(1) And MouseX()>rX#(400) And editExamine=0
   If foc=1 And blockName=0 And gotim>20 And keytim=0
    PlaySound sMenuBrowse : keytim=10 : FlushKeys()
    DrawOption(1,rX#(x),rY#(y),"Name","     ") : Flip
    Locate rX#(x)+10,rY#(y)-9 : Color 255,255,255
    SetFont font(7) 
    oldName$=charName$(0)
    charName$(0)=Left$(Input$(""),30) 
    If charName$(0)="" Then charName$(0)=oldName$
    charTeamName$(0)=charName$(0)+"'s Team"
   EndIf
  EndIf
 EndIf
 ;2. ATTRIBUTES
 If page=2
  ;main options
  x=575 : y=130 : pointsY=0
  DrawOption(1,rX#(x),rY#(y),"Design",charDesign(0)+"%") : DrawImage gIconSmall(1),rX#(x),rY#(y) : editPointsY(1)=rY#(y) : y=y+40
  DrawOption(2,rX#(x),rY#(y),"Programming",charProgram(0)+"%") : DrawImage gIconSmall(2),rX#(x),rY#(y) : editPointsY(2)=rY#(y) : y=y+45
  DrawOption(3,rX#(x),rY#(y),"2D Art",char2D(0)+"%") : DrawImage gIconSmall(3),rX#(x),rY#(y) : editPointsY(3)=rY#(y) : y=y+40
  DrawOption(4,rX#(x),rY#(y),"3D Art",char3D(0)+"%") : DrawImage gIconSmall(4),rX#(x),rY#(y) : editPointsY(4)=rY#(y) : y=y+45
  DrawOption(5,rX#(x),rY#(y),"Sound",charSound(0)+"%") : DrawImage gIconSmall(5),rX#(x),rY#(y) : editPointsY(5)=rY#(y) : y=y+40
  DrawOption(6,rX#(x),rY#(y),"Music",charMusic(0)+"%") : DrawImage gIconSmall(6),rX#(x),rY#(y) : editPointsY(6)=rY#(y)  : y=y+45
  DrawOption(7,rX#(x),rY#(y),"Reputation",charRep(0)+"%") : DrawImage gIconSmall(7),rX#(x),rY#(y) : editPointsY(7)=rY#(y) : y=y+45
  DrawOption(8,rX#(x),rY#(470),">>> BUILD >>>","")
  DrawOption(9,rX#(x),rY#(506),"<<< PROFILE <<<","")
  DrawOption(10,rX#(x),rY#(545),"SAVE & EXIT","")
  ;career points
  If game=1 And screenAgenda=5 And foc=>1 And foc=<6
   SetFont font(2)
   If editPoints>0 Then Outline("("+editPoints+" Points Remaining)",x,editPointsY(foc)+15,0,0,0,100,200,100)
   If editPoints=0 And flashTim=<20 Then Outline("(No Points Remaining!)",x,editPointsY(foc)+15,0,0,0,255,50,50)
  EndIf
 EndIf
 ;3. BUILD
 If page=3
  x=575 : y=130
  DrawOption(1,rX#(x),rY#(y),"Age",charAge(0)+"yrs") : y=y+35
  DrawOption(2,rX#(x),rY#(y),"Height",GetHeight$(charHeight(0))) : y=y+35
  DrawOption(3,rX#(x),rY#(y),"Build",textBuild$(charBuild(0))) : y=y+35
  DrawOption(4,rX#(x),rY#(y),"Demeanour",textEyes$(charEyes(0)))
  DrawImage gSmiley(charEyes(0)),rX#(x),rY#(y) : y=y+40
  DrawOption(5,rX#(x),rY#(y),"Headwear",textHat$(charHatStyle(0))) : y=y+35
  If charHatStyle(0)>0 Then namer$=charHat(0)+" / "+no_hats Else namer$="N/A"
  DrawOption(6,rX#(x),rY#(y),"Hat Design",namer$) : y=y+35
  DrawOption(7,rX#(x),rY#(y),"Eyewear",textSpecs$(charSpecs(0))) : y=y+35
  DrawOption(8,rX#(x),rY#(y),"Accessories",textAccessory$(charAccessory(0))) : y=y+35
  DrawOption(9,rX#(x),rY#(y),"Clothing",textBaggy$(charBaggy(0))) : y=y+40
  DrawOption(10,rX#(x),rY#(470),">>> CLOTHING >>>","")
  DrawOption(11,rX#(x),rY#(506),"<<< ATTRIBUTES <<<","")
  DrawOption(12,rX#(x),rY#(545),"SAVE & EXIT","")
 EndIf
 ;4. CLOTHING
 If page=4
  x=575 : y=130
  DrawOption(1,rX#(x),rY#(y),"Hair Style",textHair$(charHairStyle(0))) : y=y+35
  DrawOption(2,rX#(x),rY#(y),"Hair Colour",charHair(0)+" / "+no_hairs) : y=y+35
  DrawOption(3,rX#(x),rY#(y),"Face",charFace(0)+" / "+no_faces) : y=y+35
  If charBeard(0)>0 Then namer$=charBeard(0)+" / "+no_beards Else namer$="None"
  DrawOption(4,rX#(x),rY#(y),"Facial Hair",namer$) : y=y+40
  DrawOption(5,rX#(x),rY#(y),"Body",charBody(0)+" / "+no_bodies) : y=y+35
  DrawOption(6,rX#(x),rY#(y),"Arms",charArms(0)+" / "+no_arms) : y=y+35
  If charShorts(0)>0 Then namer$=charShorts(0)+" / "+no_legs Else namer$="None"
  DrawOption(7,rX#(x),rY#(y),"Shorts",namer$) : y=y+35
  DrawOption(8,rX#(x),rY#(y),"Legs",charLegs(0)+" / "+no_legs) : y=y+35
  DrawOption(9,rX#(x),rY#(y),"Shoes",charShoes(0)+" / "+no_legs) : y=y+40 
  DrawOption(10,rX#(x),rY#(470),">>> PROFILE >>>","")
  DrawOption(11,rX#(x),rY#(506),"<<< BUILD <<<","")
  DrawOption(12,rX#(x),rY#(545),"SAVE & EXIT","")
 EndIf
 ;advice & warnings
 SetFont font(2) 
 If warnX>0 And warnY>0
  If flashTim=<20 Then Outline(warning$,warnX,warnY+16,0,0,0,255,50,50)
 Else
  If adviceX>0 And adviceY>0 And flashTim=<20 Then Outline(advice$,adviceX,adviceY+16,0,0,0,100,200,100)
 EndIf
 ;costume expenses
 If go=0 And game=1 And screenAgenda<>5
  x=rX#(740) : y=rY#(45)
  DisplayMoney(gamBank,x,y)
  If gamShopping>0
   SetFont fontNews(4)
   Outline("-$"+GetFigure$(gamShopping),x,y+48,0,0,0,200,100,100)
  EndIf
 EndIf
 ;loading call
 If screenCall=1
  ;QuickLoader(rX#(400),rY#(300),"Please Wait","Reloading Character")
  Loader("Please Wait","Reloading Character")
  FreeEntity p(cyc)
  LoadMuse(cyc)
  oldState=-1
 EndIf
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf  
 If go=0
  DrawImage gCursor,MouseX(),MouseY()
  If editExamine=0 And MouseX()>rX#(120) And MouseX()<rX#(300) And MouseY()>rY#(100) And MouseY()<rY#(550)
   SetFont font(3)
   Outline("CLICK TO EXAMINE",MouseX()+5,MouseY()-15,0,0,0,100,200,100)
  EndIf
 EndIf

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
If go>0 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go>0 And gamShopping>0 Then PlaySound sCash : gamBank=gamBank-gamShopping
;save changes
If go>0
 ;update mask
 If optWindow=0 Then Loader("Please Wait","Saving Character")
 GetColor 1,1
 charPhotoR(editChar)=ColorRed()
 charPhotoG(editChar)=ColorGreen()
 charPhotoB(editChar)=ColorBlue()
 ;take photo
 photoX#=210 : photoY#=285
 charPhoto(editChar)=CreateImage(rY#(330),rY#(540))
 ;charPhoto(editChar)=CreateImage(PercentOf#(rY#(600)-60,61),rY#(600)-60)
 GrabImage charPhoto(editChar),rX#(photoX#),rY#(photoY#)
 MaskImage charPhoto(editChar),charPhotoR(editChar),charPhotoG(editChar),charPhotoB(editChar)
 ;save new image
 Loader("Please Wait","Saving Character")
 ResizeImage charPhoto(editChar),75,128
 SaveImage(charPhoto(editChar),"Data/Slot0"+slot+"/Portraits/Photo"+Dig$(editChar,100)+".bmp")
 ;translate changes 
 CopyChar(0,editChar)
 If screenAgenda=5 Then ResetOldValues(editChar)
 ;switch rosters
 If editChar=no_chars+1
  no_chars=no_chars+1
  fedSize(charFed(editChar))=fedSize(charFed(editChar))+1
  fedRoster(charFed(editChar),fedSize(charFed(editChar)))=editChar
 EndIf
 If charFed(editChar)<>charFed(0) Then MoveChar(editChar,charFed(0))
 ;save changes
 SaveWorld(slot)
 SaveChar(editChar,slot)
EndIf
;free entities
FreeTimer timer
FreeEntity cam
FreeEntity light
FreeEntity p(cyc)
For limb=1 To 50
 If pShadow(cyc,limb)>0
  FreeEntity pShadow(cyc,limb)
 EndIf
Next
;restore textures
If go=<0 Then Loader("Please Wait","Closing Editor")
RestoreTextures()
;proceed
screen=12 : fed=editFed
If game=1 Then screen=20
If screenAgenda=5 
 If go=-1 Then slotActive(slot)=0 : screen=10
 If go=1 And optCutscenes=2 Then negTopic=0 : negChar=fedBooker(no_feds) : negSetting=1 : screen=53
 If go=1 And optCutscenes=<1 Then screen=20 
EndIf
If go=2 Then screen=11 : screenAgenda=7
End Function 

;-----------------------------------------------------------------
;////////////////////// RELATED FUNCTIONS ////////////////////////
;-----------------------------------------------------------------

;FIND ADVICE RELATED TO BOX
Function FindAdvice(x,y)
 ;editing
 If screen=51
  ;profile
  If page=1
   If foc=1 And blockName=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=>2 And foc=<4 And blockContract=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=5 And game=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=5 And blockData=0 And screenAgenda<>5 Then advice$="(Press SPACE to randomize)" : adviceX=x : adviceY=y
  EndIf 
  ;attributes
  If page=2
   If foc=7 And screenAgenda=5 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=>1 And foc=<7 And blockData=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
  EndIf
  ;build
  If page=3
   If foc=>1 And foc=<2 And blockData=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=3
    If blockData=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
    If editModel<>charBuild(0) Then advice$="(Press ENTER to update model)" : adviceX=x : adviceY=y
   EndIf
   If foc=4 And blockDemeanour=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=>5 And foc=<9 And blockCostume=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
  EndIf
  ;clothing
  If page=4
   If foc=>1 And foc=<2 And blockHair=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=3 And blockData=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
   If foc=>4 And foc=<9 And blockCostume=1 Then warnX=x : warnY=y : warning$="(This is beyond your control)"
  EndIf
 EndIf
End Function

;INITIALISE CREATION MODEL
Function LoadMuse(cyc)
 ;load model
 editModel=charBuild(pChar(cyc))
 p(cyc)=LoadAnimMesh("World/Workstations/"+textBuild$(editModel)+".3ds")
 PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc) 
 RotateEntity p(cyc),0,345,0
 ;load animations
 pSeq(cyc,1000)=LoadAnimSeq(p(cyc),"Characters/Animation/Female.3ds") 
 pSeq(cyc,1002)=LoadAnimSeq(p(cyc),"Characters/Animation/Dancing.3ds")
 pSeq(cyc,1010)=LoadAnimSeq(p(cyc),"Characters/Animation/Gestures.3ds")  
 ;stances
 pSeq(cyc,1)=ExtractAnimSeq(p(cyc),135,175,pSeq(cyc,1000)) ;feminine stance 
 pSeq(cyc,2)=ExtractAnimSeq(p(cyc),490,530,pSeq(cyc,1002)) ;normal stance
 pSeq(cyc,3)=ExtractAnimSeq(p(cyc),2110,2170,pSeq(cyc,1010)) ;deep thought
 pSeq(cyc,4)=ExtractAnimSeq(p(cyc),160,220,pSeq(cyc,1002)) ;hands on hips
 pSeq(cyc,5)=ExtractAnimSeq(p(cyc),2020,2080,pSeq(cyc,1010)) ;folded arms
 ;reset state
 pAnim(cyc)=2 : pState(cyc)=2
 Animate p(cyc),1,0.5,pSeq(cyc,pState(cyc)),0
 ApplyHandhelds(cyc)
End Function

;IDENTIFY LIMBS
Function GetLimbs(cyc)
 ;reset entries
 For count=1 To 50
  pLimb(cyc,count)=0 
 Next
 ;head
 pLimb(cyc,1)=FindChild(p(cyc),"Head")
 pLimb(cyc,2)=FindChild(p(cyc),"L_Ear")
 pLimb(cyc,3)=FindChild(p(cyc),"R_Ear")
 pLimb(cyc,4)=FindChild(p(cyc),"Neck")
 ;body
 If charBaggy(pChar(cyc))=1 Or charBaggy(pChar(cyc))=3
  pLimb(cyc,5)=FindChild(p(cyc),"Body_Bag")
 Else
  pLimb(cyc,5)=FindChild(p(cyc),"Body")
 EndIf 
 ;left arm
 pLimb(cyc,6)=FindChild(p(cyc),"L_Bicep")
 pLimb(cyc,7)=FindChild(p(cyc),"L_Arm")
 pLimb(cyc,8)=FindChild(p(cyc),"L_Hand")
 pLimb(cyc,9)=FindChild(p(cyc),"L_Thumb01")
 pLimb(cyc,10)=FindChild(p(cyc),"L_Thumb02")
 For count=1 To 8
  pLimb(cyc,10+count)=FindChild(p(cyc),"L_Finger0"+count)
 Next
 ;right arm
 pLimb(cyc,19)=FindChild(p(cyc),"R_Bicep")
 pLimb(cyc,20)=FindChild(p(cyc),"R_Arm")
 pLimb(cyc,21)=FindChild(p(cyc),"R_Hand")
 pLimb(cyc,22)=FindChild(p(cyc),"R_Thumb01")
 pLimb(cyc,23)=FindChild(p(cyc),"R_Thumb02")
 For count=1 To 8
  pLimb(cyc,23+count)=FindChild(p(cyc),"R_Finger0"+count)
 Next
 ;shorts
 pLimb(cyc,36)=FindChild(p(cyc),"Hips")
 pLimb(cyc,37)=FindChild(p(cyc),"L_Short")
 pLimb(cyc,38)=FindChild(p(cyc),"R_Short")
 ;legs
 pLimb(cyc,39)=FindChild(p(cyc),"L_Thigh")
 If charBaggy(pChar(cyc))=2 Or charBaggy(pChar(cyc))=3
  pLimb(cyc,40)=FindChild(p(cyc),"L_Leg_Bag")
 Else
  pLimb(cyc,40)=FindChild(p(cyc),"L_Leg")
 EndIf
 pLimb(cyc,41)=FindChild(p(cyc),"L_Foot")
 pLimb(cyc,42)=FindChild(p(cyc),"R_Thigh")  
 If charBaggy(pChar(cyc))=2 Or charBaggy(pChar(cyc))=3 
  pLimb(cyc,43)=FindChild(p(cyc),"R_Leg_Bag")
 Else
  pLimb(cyc,43)=FindChild(p(cyc),"R_Leg")
 EndIf
 pLimb(cyc,44)=FindChild(p(cyc),"R_Foot")
End Function

;MAJOR LIMB?
Function MajorLimb(limb)
 value=1
 If limb=>2 And limb=<3 Then value=0 ;ears
 If limb=>9 And limb=<18 Then value=0 ;left fingers
 If limb=>22 And limb=<31 Then value=0 ;right fingers
 If limb=>37 And limb=<38 Then value=0 ;shorts
 Return value
End Function

;DETERMINE RACE
Function GetRace(char) ;0/1=white, 2=asian, 3=black
 value=1
 If tFaceType(charFace(char))>0 Then value=tFaceType(charFace(char))
 Return value
End Function

;DETERMINE GENDER
Function GetGender(char) ;0=male, 1=female
 If char=>0 And char=<no_chars
  If charBuild(char)=0 Then value=1 Else value=0
 EndIf
 Return value
End Function

;PICK OUT HEAD IN PORTRAIT
Function PortraitHead#(char)
 factor#=GetPercent#(charHeight(char),24)
 If char=0 Then factor#=GetPercent#(12,24)
 value=Int(22.0+PercentOf#(19,factor#))
 Return value
End Function

;APPLY COSTUME
Function ApplyCostume(cyc)
 GetLimbs(cyc)
 ApplyHairstyle(cyc,charHatStyle(pChar(cyc)))
 ApplyHeadwear(cyc)
 ApplyEyewear(cyc)
 ApplyAccessories(cyc)
 ApplyClothing(cyc) 
End Function

;REMOVE ALL HAIR
Function RemoveHair(cyc)
 For limb=1 To 20
  If FindChild(p(cyc),hairFile$(limb))>0
   EntityAlpha FindChild(p(cyc),hairFile$(limb)),0
  EndIf
 Next
End Function

;APPLY HAIRSTYLE
Function ApplyHairstyle(cyc,headwear)
 ;identify variables
 char=pChar(cyc)
 ;hide all by default
 RemoveHair(cyc)
 ;determine style
 showHairA$="" : showHairB$=""
 If charHairStyle(char)=5 Then showHairA$="H_Bald" : showHairB$=""
 If charHairStyle(char)=6 Then showHairA$="H_Oaf" : showHairB$=""
 If charHairStyle(char)=7 Then showHairA$="H_Recede" : showHairB$=""
 If charHairStyle(char)=8 Then showHairA$="H_Short" : showHairB$=""
 If charHairStyle(char)=9 Then showHairA$="H_Raised" : showHairB$=""
 If charHairStyle(char)=10 Then showHairA$="H_Quiff" : showHairB$=""
 If charHairStyle(char)=11 Then showHairA$="H_Side" : showHairB$=""
 If charHairStyle(char)=12 Then showHairA$="H_Part" : showHairB$=""
 If charHairStyle(char)=13 Then showHairA$="H_Mop" : showHairB$=""
 If charHairStyle(char)=14 Then showHairA$="H_Thick" : showHairB$=""
 If charHairStyle(char)=15 Then showHairA$="H_Full" : showHairB$=""  
 If charHairStyle(char)=16 Then showHairA$="H_Curly" : showHairB$=""
 If charHairStyle(char)=17 Then showHairA$="H_Afro" : showHairB$=""
 If charHairStyle(char)=18 Then showHairA$="H_Buzz" : showHairB$=""
 If charHairStyle(char)=19 Then showHairA$="H_Spikey" : showHairB$=""
 If charHairStyle(char)=20 Then showHairA$="H_Punk" : showHairB$=""
 If charHairStyle(char)=21 Then showHairA$="H_Rows" : showHairB$=""
 If charHairStyle(char)=22 Then showHairA$="H_Bald" : showHairB$="H_Pony"
 If charHairStyle(char)=23 Then showHairA$="H_Recede" : showHairB$="H_Pony"
 If charHairStyle(char)=24 Then showHairA$="H_Short" : showHairB$="H_Pony"
 If charHairStyle(char)=25 Then showHairA$="H_Raised" : showHairB$="H_Pony"
 If charHairStyle(char)=26 Then showHairA$="H_Quiff" : showHairB$="H_Pony"
 If charHairStyle(char)=27 Then showHairA$="H_Side" : showHairB$="H_Pony"
 If charHairStyle(char)=28 Then showHairA$="H_Part" : showHairB$="H_Pony"
 If charHairStyle(char)=29 Then showHairA$="H_Mop" : showHairB$="H_Pony"  
 If charHairStyle(char)=30 Then showHairA$="H_Thick" : showHairB$="H_Pony"
 If charHairStyle(char)=31 Then showHairA$="H_Curly" : showHairB$="H_Pony"
 If charHairStyle(char)=32 Then showHairA$="H_Punk" : showHairB$="H_Pony"
 If charHairStyle(char)=33 Then showHairA$="H_Rows" : showHairB$="H_Pony"
 If charHairStyle(char)=34 Then showHairA$="H_Bald" : showHairB$="H_Long"
 If charHairStyle(char)=35 Then showHairA$="H_Recede" : showHairB$="H_Long"
 If charHairStyle(char)=36 Then showHairA$="H_Short" : showHairB$="H_Long"
 If charHairStyle(char)=37 Then showHairA$="H_Raised" : showHairB$="H_Long"
 If charHairStyle(char)=38 Then showHairA$="H_Quiff" : showHairB$="H_Long"
 If charHairStyle(char)=39 Then showHairA$="H_Side" : showHairB$="H_Long"
 If charHairStyle(char)=40 Then showHairA$="H_Part" : showHairB$="H_Long"
 If charHairStyle(char)=41 Then showHairA$="H_Mop" : showHairB$="H_Long"
 If charHairStyle(char)=42 Then showHairA$="H_Thick" : showHairB$="H_Long"  
 If charHairStyle(char)=43 Then showHairA$="H_Curly" : showHairB$="H_Long"
 If charHairStyle(char)=44 Then showHairA$="H_Punk" : showHairB$="H_Long"
 If charHairStyle(char)=45 Then showHairA$="H_Rows" : showHairB$="H_Long"
 ;hat logic
 If showHairA$<>""
  If headwear=1
   If showHairA$="H_Side" Or showHairA$="H_Mop" Then showHairA$="H_Part"
  EndIf
  If headwear=2 Then showHairA$="" : showHairB$=""
  If headwear=>3 And headwear=<9
   If showHairA$="H_Buzz"
    showHairA$=""
   Else
    showHairA$="H_Bald"
    If charHairStyle(char)=6 Or charHairStyle(char)=15 Then showHairA$="H_Oaf"
   EndIf
  EndIf
 EndIf
 ;compose hair
 If showHairA$<>""
  EntityAlpha FindChild(p(cyc),showHairA$),1
  EntityTexture FindChild(p(cyc),showHairA$),tHair(charHair(char));,0,1
  tHairAccess(charHair(char))=1
 EndIf
 If showHairB$<>""
  EntityAlpha FindChild(p(cyc),showHairB$),1 
  EntityTexture FindChild(p(cyc),showHairB$),tHair(charHair(char));,0,1
  tHairAccess(charHair(char))=1
  If showHairB$="H_Pony" Then EntityTexture FindChild(p(cyc),showHairB$),tPony,0,2
 EndIf
 ;add shaved layer
 EntityTexture FindChild(p(cyc),"Head"),tMouth(0),0,2
 If charHairStyle(char)=>1 And charHairStyle(char)=<4
  EntityTexture FindChild(p(cyc),"Head"),tShaved(charHairStyle(char)),0,2
 EndIf
 If charHairStyle(char)=46 Then EntityTexture FindChild(p(cyc),"Head"),tShaved(5),0,2
 If charHairStyle(char)=47 Then EntityTexture FindChild(p(cyc),"Head"),tShaved(6),0,2
 If showHairA$="H_Buzz" Or showHairA$="H_Rows" Then EntityTexture FindChild(p(cyc),"Head"),tShaved(3),0,2
End Function

;REMOVE ALL HEADWEAR
Function RemoveHeadwear(cyc)
 HideEntity FindChild(p(cyc),"Band01")
 HideEntity FindChild(p(cyc),"Band02")
 HideEntity FindChild(p(cyc),"Bandana")
 HideEntity FindChild(p(cyc),"Cap")
 HideEntity FindChild(p(cyc),"Hat")
End Function

;APPLY HEADWEAR
Function ApplyHeadwear(cyc)
 ;identify variables
 char=pChar(cyc)
 ;hide by default
 RemoveHeadwear(cyc)
 ;headband
 If charHatStyle(char)=1
  For count=1 To 2
   ShowEntity FindChild(p(cyc),"Band0"+count)
   EntityTexture FindChild(p(cyc),"Band0"+count),tHat(charHat(char)),0,1 
   tHatAccess(charHat(char))=1
  Next
  If charHairStyle(char)=>5 And charHairStyle(char)<>46 And charHairStyle(char)<>47
   If charHairStyle(char)<>18 And charHairStyle(char)<>20 And charHairStyle(char)<>21 And charHairStyle(char)<>32 And charHairStyle(char)<>33 And charHairStyle(char)<>44 And charHairStyle(char)<>45
    HideEntity FindChild(p(cyc),"Band02")
   EndIf
  EndIf
 EndIf
 ;bandana
 If charHatStyle(char)=2
  HideEntity FindChild(p(cyc),"L_Ear")
  HideEntity FindChild(p(cyc),"R_Ear")
  ShowEntity FindChild(p(cyc),"Bandana")
  EntityTexture FindChild(p(cyc),"Bandana"),tHat(charHat(char)),0,1 
  tHatAccess(charHat(char))=1
 Else
  ShowEntity FindChild(p(cyc),"L_Ear")
  ShowEntity FindChild(p(cyc),"R_Ear")
 EndIf
 ;caps
 If charHatStyle(char)=>3 And charHatStyle(char)=<6
  ShowEntity FindChild(p(cyc),"Cap")
  EntityTexture FindChild(p(cyc),"Cap"),tHat(charHat(char)),0,1
  tHatAccess(charHat(char))=1
  PositionEntity FindChild(p(cyc),"Cap"),-0.33,34.39,3.45 
  If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Cap"),-0.33,34.39,6.6
  RotateEntity FindChild(p(cyc),"Cap"),0,0,0
  If charHatStyle(char)=4 ;tipped variation
   PositionEntity FindChild(p(cyc),"Cap"),1.03,41.19,5.06 
   If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Cap"),2.11,39.19,9.73 
   RotateEntity FindChild(p(cyc),"Cap"),18.60,-21.79,0
  EndIf
  If charHatStyle(char)=5 ;raised variation
   PositionEntity FindChild(p(cyc),"Cap"),-1.15,37.03,1.97 
   If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Cap"),-0.52,35.03,5.61
   RotateEntity FindChild(p(cyc),"Cap"),-25.1,12.5,0
  EndIf
  If charHatStyle(char)=6 ;reversed variation
   PositionEntity FindChild(p(cyc),"Cap"),0.05,32.4,3.54
   If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Cap"),0.05,30.4,6.54 
   RotateEntity FindChild(p(cyc),"Cap"),23.77,180.9,0
  EndIf
 EndIf
 ;hats
 If charHatStyle(char)=>7 And charHatStyle(char)=<9
  ShowEntity FindChild(p(cyc),"Hat")
  EntityTexture FindChild(p(cyc),"Hat"),tHat(charHat(char)),0,1
  tHatAccess(charHat(char))=1
  PositionEntity FindChild(p(cyc),"Hat"),-0.33,34.39,3.45 
  If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Hat"),-0.33,34.39,6.6
  RotateEntity FindChild(p(cyc),"Hat"),0,0,0
  If charHatStyle(char)=8 ;tipped variation
   PositionEntity FindChild(p(cyc),"Hat"),2.43,41.62,5.14 
   If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Hat"),2.43,39.62,6.14 
   RotateEntity FindChild(p(cyc),"Hat"),20,-18.9,-7.4
   If charSpecs(char)>0
    PositionEntity FindChild(p(cyc),"Hat"),2.43,42.16,5.14  
    RotateEntity FindChild(p(cyc),"Hat"),16.9,-18.9,-7.4
   EndIf
  EndIf
  If charHatStyle(char)=9 ;raised variation
   PositionEntity FindChild(p(cyc),"Hat"),1.69,40.73,1.22 
   If charHairStyle(char)=<4 Or charHairStyle(char)=46 Or charHairStyle(char)=47 Then PositionEntity FindChild(p(cyc),"Hat"),1.69,36.44,4.45
   RotateEntity FindChild(p(cyc),"Hat"),-21.47,2.4,0
  EndIf
 EndIf
End Function

;REMOVE ALL EYEWEAR
Function RemoveEyewear(cyc)
 HideEntity FindChild(p(cyc),"Specs")
 HideEntity FindChild(p(cyc),"Lens01")
 HideEntity FindChild(p(cyc),"Lens02")
 HideEntity FindChild(p(cyc),"Patch")
End Function

;APPLY EYEWEAR
Function ApplyEyewear(cyc)
 ;identify variables
 char=pChar(cyc)
 ;hide by default
 RemoveEyewear(cyc)
 If charSpecs(char)=>1 And charSpecs(char)=<7
  ;compose specs
  ShowEntity FindChild(p(cyc),"Specs")
  EntityShininess FindChild(p(cyc),"Specs"),0.5
  For count=1 To 2
   ShowEntity FindChild(p(cyc),"Lens0"+count)
   EntityColor FindChild(p(cyc),"Lens0"+count),255,255,255
   EntityAlpha FindChild(p(cyc),"Lens0"+count),0.2
   EntityShininess FindChild(p(cyc),"Lens0"+count),1
  Next
  ;golden frame
  If charSpecs(char)=1 
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(1)
  EndIf
  ;silver frame
  If charSpecs(char)=2 
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(2)
  EndIf
  ;black frame
  If charSpecs(char)=3 
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(3)
  EndIf
  ;amber lenses
  If charSpecs(char)=4 
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(1)
   For count=1 To 2
    EntityColor FindChild(p(cyc),"Lens0"+count),100,30,0
    EntityAlpha FindChild(p(cyc),"Lens0"+count),0.5
   Next
  EndIf
  ;yellow lenses
  If charSpecs(char)=5 
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(3)
   For count=1 To 2
    EntityColor FindChild(p(cyc),"Lens0"+count),255,240,0
    EntityAlpha FindChild(p(cyc),"Lens0"+count),0.5
   Next
  EndIf
  ;black shades
  If charSpecs(char)=6
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(3)
   For count=1 To 2
    EntityColor FindChild(p(cyc),"Lens0"+count),0,0,0
    EntityAlpha FindChild(p(cyc),"Lens0"+count),0.75
   Next
  EndIf
  ;gold-rimmed shades
  If charSpecs(char)=7
   EntityTexture FindChild(p(cyc),"Specs"),tSpecs(1)
   For count=1 To 2
    EntityColor FindChild(p(cyc),"Lens0"+count),0,0,0
    EntityAlpha FindChild(p(cyc),"Lens0"+count),0.75
   Next
  EndIf
 EndIf
 ;eye-patch
 If charSpecs(char)=8
  ShowEntity FindChild(p(cyc),"Patch")
 EndIf
End Function

;REMOVE ALL ACCESSORIES
Function RemoveAccessories(cyc)
 HideEntity FindChild(p(cyc),"Necklace")
 HideEntity FindChild(p(cyc),"Bracelet")
 HideEntity FindChild(p(cyc),"Watch")
 HideEntity FindChild(p(cyc),"Earring01")
 HideEntity FindChild(p(cyc),"Earring02")
 HideEntity FindChild(p(cyc),"Ring01")
 HideEntity FindChild(p(cyc),"Ring02")
End Function

;APPLY EYEWEAR
Function ApplyAccessories(cyc)
 ;identify variables
 char=pChar(cyc)
 ;hide by default
 RemoveAccessories(cyc)
 ;rings
 If charAccessory(char)=1 Or charAccessory(char)=6 Or charAccessory(char)=7
  For count=1 To 2
   ShowEntity FindChild(p(cyc),"Ring0"+count)
   EntityShininess FindChild(p(cyc),"Ring0"+count),1.0
  Next
 EndIf
 ;earrings
 If charAccessory(char)=2 Or charAccessory(char)=7
  For count=1 To 2
   ShowEntity FindChild(p(cyc),"Earring0"+count)
   EntityShininess FindChild(p(cyc),"Earring0"+count),1.0
  Next
 EndIf
 ;necklace
 If charAccessory(char)=3 Or charAccessory(char)=6 Or charAccessory(char)=7
  ShowEntity FindChild(p(cyc),"Necklace")
  EntityShininess FindChild(p(cyc),"Necklace"),1.0
 EndIf
 ;bracelet
 If charAccessory(char)=4 Or charAccessory(char)=6 Or charAccessory(char)=7
  ShowEntity FindChild(p(cyc),"Bracelet")
  EntityShininess FindChild(p(cyc),"Bracelet"),1.0
 EndIf
 ;watch
 If charAccessory(char)=5 Or charAccessory(char)=6 Or charAccessory(char)=7
  ShowEntity FindChild(p(cyc),"Watch")
  EntityShininess FindChild(p(cyc),"Watch"),1.0
 EndIf
 
End Function

;APPLY HANDHELDS
Function ApplyHandhelds(cyc)
 ;hide by default
 For v=1 To no_machines
  If FindChild(p(cyc),hardModel$(v))>0
   ;HideEntity FindChild(p(cyc),hardModel$(v))
   EntityAlpha FindChild(p(cyc),hardModel$(v)),0 
   HideEntity FindChild(p(cyc),hardModel$(v)+"S")
  EndIf
 Next
 ;show as necessary
 If screenAgenda=8 And pChar(cyc)=gamChar Then hardy=gamNewHardware Else hardy=gamHardware
 If (pAnim(cyc)=>16 And pAnim(cyc)=<18) Or (pAnim(cyc)=>26 And pAnim(cyc)=<28)
  If FindChild(p(cyc),hardModel$(hardy))>0
   ;ShowEntity FindChild(p(cyc),hardModel$(hardy))
   EntityAlpha FindChild(p(cyc),hardModel$(hardy)),1
   If (charTeam(pChar(cyc))=gamChar And HideScreen(cyc)) Or screenAgenda=8
    HideEntity FindChild(p(cyc),hardModel$(hardy)+"S")
   Else
    ShowEntity FindChild(p(cyc),hardModel$(hardy)+"S")
    If projIdentity(gamProject)=>0
     tScreenshot=LoadTexture("Games/Screenshots/Screen"+Dig$(projIdentity(gamProject),100)+".JPG")
    Else
     tScreenshot=LoadTexture("Games/Development/Screen"+Dig$(MakePositive#(projIdentity(gamProject)),10)+".JPG")
    EndIf
    EntityTexture FindChild(p(cyc),hardModel$(hardy)+"S"),tScreenshot
    EntityFX FindChild(p(cyc),hardModel$(hardy)+"S"),9 
    FreeTexture tScreenshot
   EndIf
  EndIf
 EndIf
 ;game box
 tBoxArt=0
 If projIdentity(gamProject)>0
  tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(projIdentity(gamProject),100)+".JPG")
 EndIf
 If projIdentified(gamProject)>0
  tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(projIdentified(gamProject),100)+".JPG")
 EndIf
 If screen=53 And negTopic=>2 And negTopic=<3
  tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(negVariable,100)+".JPG")
 EndIf
 If screen=53 And negTopic=4
  tBoxArt=LoadTexture("Games/Box Art/Box"+Dig$(projIdentity(negVariable),100)+".JPG")
 EndIf
 If tBoxArt>0
  EntityTexture FindChild(p(cyc),"Box"),tBoxArt
  FreeTexture tBoxArt
 EndIf
 If pAnim(cyc)<>15 And pAnim(cyc)<>25 Then HideEntity FindChild(p(cyc),"Box")
End Function

;APPLY CLOTHING
Function ApplyClothing(cyc)
 ;identify variables
 char=pChar(cyc)
 ;head
 For limb=1 To 4
  EntityTexture pLimb(cyc,limb),tFace(charFace(char)),0,0 
  tFaceAccess(charFace(char))=1
  If limb=>2 And limb=<3 Then EntityTexture pLimb(cyc,limb),tEars,0,3
 Next
 ;facial hair
 limb=1
 If charBeard(char)>0
  EntityTexture pLimb(cyc,limb),tBeard(charBeard(char)),0,1
 Else
  EntityTexture pLimb(cyc,limb),tMouth(0),0,1
 EndIf
 ;body
 limb=5
 EntityTexture pLimb(cyc,limb),tMouth(0),0,2
 EntityTexture pLimb(cyc,limb),tBody(charBody(char)),0,1
 tBodyAccess(charBody(char))=1
 If GetRace(char)>1 And tBodyType(charBody(char))>0
  EntityTexture pLimb(cyc,limb),tBodyShade(tBodyType(charBody(char))+(GetRace(char)-2)),0,2  
  tBodyShadeAccess(tBodyType(charBody(char))+(GetRace(char)-2))=1
 EndIf
 If charBaggy(char)=1 Or charBaggy(char)=3
  EntityAlpha FindChild(p(cyc),"Body_Bag"),1
  EntityAlpha FindChild(p(cyc),"Body"),0
 Else
  EntityAlpha FindChild(p(cyc),"Body"),1
  EntityAlpha FindChild(p(cyc),"Body_Bag"),0
 EndIf
 ;arms
 For limb=6 To 31
  If (limb=>8 And limb=<18) Or (limb=>21 And limb=<31)
   EntityTexture pLimb(cyc,limb),tMouth(0),0,2
   EntityTexture pLimb(cyc,limb),tHands(charHands(char)),0,1
   tHandsAccess(charHands(char))=1
   If GetRace(char)>1 And tHandType(charHands(char))>0
    EntityTexture pLimb(cyc,limb),tHandShade(tHandType(charHands(char))+(GetRace(char)-2)),0,2
    tHandShadeAccess(tHandType(charHands(char))+(GetRace(char)-2))=1
   EndIf
  Else
   EntityTexture pLimb(cyc,limb),tMouth(0),0,2
   EntityTexture pLimb(cyc,limb),tArm(charArms(char)),0,1
   tArmAccess(charArms(char))=1
   If GetRace(char)>1 And tArmType(charArms(char))>0
    EntityTexture pLimb(cyc,limb),tArmShade(tArmType(charArms(char))+(GetRace(char)-2)),0,2
    tArmShadeAccess(tArmType(charArms(char))+(GetRace(char)-2))=1
   EndIf
  EndIf
 Next
 ;legs
 For limb=36 To 44
  EntityTexture pLimb(cyc,limb),tMouth(0),0,2
  EntityTexture pLimb(cyc,limb),tLegs(charLegs(char)),0,1
  tLegsAccess(charLegs(char))=1 
  If GetRace(char)>1 And tLegType(charLegs(char))>0
   EntityTexture pLimb(cyc,limb),tLegShade(tLegType(charLegs(char))+(GetRace(char)-2)),0,2 
   tLegShadeAccess(tLegType(charLegs(char))+(GetRace(char)-2))=1
  EndIf
 Next
 If charBaggy(char)=2 Or charBaggy(char)=3
  EntityAlpha FindChild(p(cyc),"L_Leg_Bag"),1
  EntityAlpha FindChild(p(cyc),"R_Leg_Bag"),1
  EntityAlpha FindChild(p(cyc),"L_Leg"),0
  EntityAlpha FindChild(p(cyc),"R_Leg"),0
 Else
  EntityAlpha FindChild(p(cyc),"L_Leg"),1
  EntityAlpha FindChild(p(cyc),"R_Leg"),1
  EntityAlpha FindChild(p(cyc),"L_Leg_Bag"),0
  EntityAlpha FindChild(p(cyc),"R_Leg_Bag"),0
 EndIf
 ;shorts
 If charShorts(char)>0
  For limb=36 To 38
   EntityTexture pLimb(cyc,limb),tMouth(0),0,2
   EntityTexture pLimb(cyc,limb),tLegs(charShorts(char)),0,1 
   tLegsAccess(charShorts(char))=1
  Next
  EntityAlpha FindChild(p(cyc),"L_Short"),1
  EntityAlpha FindChild(p(cyc),"R_Short"),1
 Else
  EntityAlpha FindChild(p(cyc),"L_Short"),0
  EntityAlpha FindChild(p(cyc),"R_Short"),0
 EndIf
 ;shoes
 If charShoes(char)<>charLegs(char)
  For count=1 To 2
   If count=1 Then limb=41 Else limb=44
   EntityTexture pLimb(cyc,limb),tMouth(0),0,2
   EntityTexture pLimb(cyc,limb),tLegs(charShoes(char)),0,1
   tLegsAccess(charShoes(char))=1
   If GetRace(char)>1 And tLegType(charShoes(char))>0
    EntityTexture pLimb(cyc,limb),tLegShade(tLegType(charShoes(char))+(GetRace(char)-2)),0,2 
    tLegShadeAccess(tLegType(charShoes(char))+(GetRace(char)-2))=1
   EndIf
  Next
 EndIf
End Function

;TEXTURE SUITABLE FOR SHORTS?
Function ShortsViable(option)
 viable=0
 If option=0 Or tLegType(option)=0 Then viable=1
 If tLegType(option)=5 Or tLegType(option)=7 Or tLegType(option)=9 Or tLegType(option)=11 Or tLegType(option)=19 Then viable=1
 Return viable
End Function

;CALCULATE COSTUME EXPENSES
Function FindCostumeChanges()
 value=0
 If game=1 And screenAgenda<>5
  If charHair(0)<>charHair(gamChar) Then value=value+100
  If charHairStyle(0)<>charHairStyle(gamChar) Then value=value+100
  If charBeard(0)<>charBeard(gamChar) Then value=value+100
  If charHatStyle(0)>0 And charHatStyle(0)<>charHatStyle(gamChar) Then value=value+100
  If charHatStyle(0)>0 And charHatStyle(0)=charHatStyle(gamChar) And charHat(0)<>charHat(gamChar) Then value=value+100
  If charSpecs(0)>0 And charSpecs(0)<>charSpecs(gamChar) Then value=value+100
  If charAccessory(0)>0 And charAccessory(0)<>charAccessory(gamChar) Then value=value+100
  ;clothing
  If charBody(0)<>charBody(gamChar) Then value=value+100
  If charArms(0)<>charArms(gamChar) Then value=value+100
  ;If charHands(0)<>charHands(gamChar) Then value=value+100
  If charShorts(0)>0 And charShorts(0)<>charShorts(gamChar) And charShorts(0)<>charLegs(gamChar) And charShorts(0)<>charLegs(0) Then value=value+100 
  If charLegs(0)<>charLegs(gamChar) Then value=value+100
  If charShoes(0)<>charLegs(0) And charShoes(0)<>charShoes(gamChar) Then value=value+100 
 EndIf
 Return value
End Function

;LOAD SHADOWS
Function LoadShadows(cyc)
 Loader("","")
 For limb=1 To 50
  pShadow(cyc,limb)=0
  If (optShadows>0 And limb=36) Or (optShadows=2 And (limb=1 Or (limb=>6 And limb=<8) Or (limb=>19 And limb=<21) Or limb=40 Or limb=41 Or limb=43 Or limb=44))
   ;load sprite
   pShadow(cyc,limb)=LoadSprite("World/Sprites/Shadow.png",2)
   ScaleSprite pShadow(cyc,limb),13,13
   If limb<>36 Then ScaleSprite pShadow(cyc,limb),10,10
   If limb=8 Or limb=21 Or limb=41 Or limb=44 Then ScaleSprite pShadow(cyc,limb),8,8
   RotateEntity pShadow(cyc,limb),90,0,0
   SpriteViewMode pShadow(cyc,limb),2
   ;set transparency
   EntityAlpha pShadow(cyc,limb),0.1
   If optShadows=2 And (limb=36 Or limb=8 Or limb=21) Then EntityAlpha pShadow(cyc,limb),0.2
   If optShadows=1 Then EntityAlpha pShadow(cyc,limb),0.5
   If optShadows=0 Then EntityAlpha pShadow(cyc,limb),0 
   EntityColor pShadow(cyc,limb),10,10,10
  EndIf
 Next 
End Function