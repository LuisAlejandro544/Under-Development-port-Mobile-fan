;//////////////////////////////////////////////////////////////////////////////
;------------------------ UNDER DEVELOPMENT: SOFTWARE DATA --------------------
;//////////////////////////////////////////////////////////////////////////////

;///////////////////////////////////////////////////////
;--------------------- SOFTWARE ------------------------
;///////////////////////////////////////////////////////
.Software
Dim softTitle$(10000)
Dim softDated(10000)
Dim softControversial(10000)
Dim softBox(10000)
Dim softScreen(10000)
Dim softGameplay(10000) ;40=Atari, 50=Amstrad, 60=8-bit, 70=SNES, 80=PS, 90=PS2
Dim softReplay(10000) ;+5/10 for computers
Dim soft2D(10000) ;40=Atari, 50=Amstrad, 60=8-bit, 70=SNES, 80=PS, 90=PS2
Dim soft3D(10000) ;25=Atari, 35=Amstrad, 40=8-bit, 50=SNES, 60=Amiga, 70=PS, 80=PS2, 90=PS3
Dim softSound(10000) ;40=Atari, 50=Amstrad, 60=8-bit, 70=SNES, 80=PS, 90=PS2
Dim softMusic(10000)
;count files
Global no_games=-1
folder=ReadDir(CurrentDir$()+"Games/Box Art/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,3))
  If FileType(CurrentDir$()+"Games/Box Art/"+filename$)=1 And namer$="BOX" And no_games<10000 Then no_games=no_games+1
 Until filename$=""
CloseDir(folder)

;//////////////////////////////////////////////////////////////////////////////
;---------------------------- 17. SOFTWARE LIBRARY ----------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SoftwareLibrary()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
optGrid=1 : scroll=editScroll
no_boxes=no_games
foc=editChar : oldfoc=foc
lastfoc=editChar
go=0 : gotim=0 : keytim=10
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	flashTim=flashTim+1
	If flashTim>20 Then flashTim=0   
	
	;PORTAL
	gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then PlaySound sMenuBack : go=-1 
	 If MouseDown(1) And foc=999 Then PlaySound sMenuBack : go=-1
	 ;access
	 If MouseDown(1) And foc=>1 And foc=<no_boxes And subFoc=-1 Then PlaySound sMenuGo : editChar=foc : editScroll=scroll : go=1 
	EndIf
	;manage music
	ManageMusic(0)
	
	;SCROLLING
	If optGrid=1
	 no_displays=(Int((rY#(525)-rY#(210))/38)+1)*3
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=1
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=1
	  EndIf
	  If scroll>0 Then scroll=0
	  excess=Int(((no_boxes-no_displays)-1)/3)+1
	  If scroll<-(excess*38) Then scroll=-(excess*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   excess=Int(((foc-no_displays)-1)/3)+1
	   If foc>no_displays And scroll>-(excess*38) Then scroll=-(excess*38)
	   recess=Int((foc-1)/3)
	   If scroll<-(recess*38) Then scroll=-(recess*38) 
	  EndIf
     EndIf 
	EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;profile
 If lastfoc=>1 And lastfoc=<no_boxes
  DrawGameProfile(lastfoc,rX#(400),rY#(25),1)
 EndIf
 ;editing prompt
 SetFont font(2)
 Outline("Correctly numbered JPGs in the 'Games/Box Art/' folder will show up here:",rX#(400),rY#(10),0,0,0,125,100,125)
 ;scrollers
 subFoc=-1
 If optGrid=1 Then indent=345 Else indent=345
 If no_boxes>no_displays 
  If scroll<0
   y=rY#(210)+58
   If MouseY()>rY#(y)-72 And MouseY()<rY#(y)+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=20
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=20
   EndIf
   If subFoc=20
    DrawImage gScroll(3),rX#(400)-indent,rY#(y)
    DrawImage gScroll(3),rX#(400)+indent,rY#(y)
   Else
    DrawImage gScroll(1),rX#(400)-indent,rY#(y)
    DrawImage gScroll(1),rX#(400)+indent,rY#(y)
   EndIf
  EndIf
  excess=Int(((no_boxes-no_displays)-1)/3)+1
  If (optGrid=0 And scroll>-((no_boxes-no_displays)*38)) Or (optGrid=1 And scroll>-(excess*38))
   y=rY#(520)-58
   If MouseY()>rY#(y)-72 And MouseY()<rY#(y)+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=21
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=21
   EndIf
   If subFoc=21
    DrawImage gScroll(4),rX#(400)-indent,rY#(y)
    DrawImage gScroll(4),rX#(400)+indent,rY#(y)
   Else
    DrawImage gScroll(2),rX#(400)-indent,rY#(y)
    DrawImage gScroll(2),rX#(400)+indent,rY#(y)
   EndIf
  EndIf
 EndIf
 ;NAME BOXES
 foc=0
 x=rX#(400)-205 : listY=rY#(210)
 For cyc=1 To no_boxes
  y=listY+scroll
  If y=>rY#(210) And y=<rY#(525)
   ;mouse hotspots
   If MouseX()=>x-101 And MouseX()=<x+101 And MouseY()=>y-15 And MouseY()=<y+18 Then foc=cyc
   ;name box
   namer$=cyc+". "+softTitle$(cyc)
   SqueezeFont(namer$,175,20)
   If foc=cyc
    DrawImage gMenu(1),x,y 
    Outline(namer$,x,y+1,255,240,0,255,240,0)
   Else
    DrawImage gMenu(2),x,y 
    Outline(namer$,x,y+1,175,175,175,175,175,175)
   EndIf
  EndIf
  ;increment
  x=x+205
  If x>rX#(400)+300 Then x=rX#(400)-205 : listY=listY+38
 Next
 ;exit option
 DrawOption(999,rX#(400),rY#(565),"<<< BACK <<<","") 
 ;cursor
 If foc=>1 And foc=<no_boxes Then lastfoc=foc
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf
 If subFoc<>oldSubFoc
  oldSubFoc=subFoc
  If subFoc>0 Then PlaySound sMenuSelect
 EndIf
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=1 Then screen=18 Else screen=1
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------------ 18. EDIT GAME ---------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function EditGame()
;load images
Loader("Please Wait","Restoring Data")
LoadGameBox(editChar)
LoadGameScreen(editChar)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
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
	 ;leave
	 If KeyDown(1) Then PlaySound sMenuBack : go=-1
	 ;proceed
	 If MouseDown(1) And foc=10 Then PlaySound sMenuGo : go=1
	EndIf
	;manage music
	ManageMusic(0) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;search left
	 If MouseDown(2)
	  If foc=2 Then softDated(editChar)=softDated(editChar)-1 : PlaySound sMenuBrowse : keytim=10
	  If foc=3 Then softGameplay(editChar)=softGameplay(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=4 Then softReplay(editChar)=softReplay(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=5 Then soft2D(editChar)=soft2D(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=6 Then soft3D(editChar)=soft3D(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=7 Then softSound(editChar)=softSound(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=8 Then softMusic(editChar)=softMusic(editChar)-1 : PlaySound sMenuBrowse : keytim=2
	  If foc=9 Then softControversial(editChar)=softControversial(editChar)-1 : PlaySound sMenuBrowse : keytim=10
	 EndIf
	 ;search right
	 If MouseDown(1)
	  If foc=2 Then softDated(editChar)=softDated(editChar)+1 : PlaySound sMenuBrowse : keytim=10
	  If foc=3 Then softGameplay(editChar)=softGameplay(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=4 Then softReplay(editChar)=softReplay(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=5 Then soft2D(editChar)=soft2D(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=6 Then soft3D(editChar)=soft3D(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=7 Then softSound(editChar)=softSound(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=8 Then softMusic(editChar)=softMusic(editChar)+1 : PlaySound sMenuBrowse : keytim=2
	  If foc=9 Then softControversial(editChar)=softControversial(editChar)+1 : PlaySound sMenuBrowse : keytim=10
	 EndIf 
	 ;randomize
	 If KeyDown(57)
	  If foc=2 Then softDated(editChar)=Rnd(0,1) : PlaySound sMenuBrowse : keytim=10
	  If foc=3 Then softGameplay(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=4 Then softReplay(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=5 Then soft2D(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=6 Then soft3D(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=7 Then softSound(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=8 Then softMusic(editChar)=Rnd(0,99) : PlaySound sMenuBrowse : keytim=10
	  If foc=9 Then softControversial(editChar)=Rnd(0,1) : PlaySound sMenuBrowse : keytim=10 
	 EndIf
	 ;revert
	 If KeyDown(14)
	  If foc=1 Then softTitle$(editChar)="Game #"+Dig$(editChar,100) : PlaySound sTrash : keytim=10
	  If foc=2 Then softDated(editChar)=0 : PlaySound sTrash : keytim=10
	  If foc=3 Then softGameplay(editChar)=50 : PlaySound sTrash : keytim=10
	  If foc=4 Then softReplay(editChar)=50 : PlaySound sTrash : keytim=10
	  If foc=5 Then soft2D(editChar)=50 : PlaySound sTrash : keytim=10
	  If foc=6 Then soft3D(editChar)=0 : PlaySound sTrash : keytim=10
	  If foc=7 Then softSound(editChar)=50 : PlaySound sTrash : keytim=10
	  If foc=8 Then softMusic(editChar)=50 : PlaySound sTrash : keytim=10
	  If foc=9 Then softControversial(editChar)=0 : PlaySound sTrash : keytim=10 
	 EndIf
	EndIf  
	;limits
	If softDated(editChar)<0 Then softDated(editChar)=1
	If softDated(editChar)>1 Then softDated(editChar)=0 
	If softGameplay(editChar)<0 Then softGameplay(editChar)=0
	If softGameplay(editChar)>99 Then softGameplay(editChar)=99 
	If softReplay(editChar)<0 Then softReplay(editChar)=0
	If softReplay(editChar)>99 Then softReplay(editChar)=99   
	If soft2D(editChar)<0 Then soft2D(editChar)=0
	If soft2D(editChar)>99 Then soft2D(editChar)=99 
	If soft3D(editChar)<0 Then soft3D(editChar)=0
	If soft3D(editChar)>99 Then soft3D(editChar)=99 
	If softSound(editChar)<0 Then softSound(editChar)=0
	If softSound(editChar)>99 Then softSound(editChar)=99 
	If softMusic(editChar)<0 Then softMusic(editChar)=0
	If softMusic(editChar)>99 Then softMusic(editChar)=99  
	If softControversial(editChar)<0 Then softControversial(editChar)=1
	If softControversial(editChar)>1 Then softControversial(editChar)=0   
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawGameProfile(editChar,rX#(400),rY#(25),1)
 ;options
 foc=0
 x=400 : y=210
 DrawOption(1,rX#(x),rY#(y),"Title",softTitle$(editChar)) : y=y+38
 DrawOption(2,rX#(x),rY#(y),"Dated",textYesNo$(softDated(editChar))) : y=y+42
 DrawOption(3,rX#(x),rY#(y),"Gameplay",softGameplay(editChar)+"%") : DrawImage gIconSmall(1),rX#(x),rY#(y) : y=y+38
 DrawOption(4,rX#(x),rY#(y),"Replay",softReplay(editChar)+"%") : DrawImage gIconSmall(2),rX#(x),rY#(y) : y=y+38
 DrawOption(5,rX#(x),rY#(y),"2D Graphics",soft2D(editChar)+"%") : DrawImage gIconSmall(3),rX#(x),rY#(y) : y=y+38
 DrawOption(6,rX#(x),rY#(y),"3D Graphics",soft3D(editChar)+"%") : DrawImage gIconSmall(4),rX#(x),rY#(y) : y=y+38
 DrawOption(7,rX#(x),rY#(y),"Sound",softSound(editChar)+"%") : DrawImage gIconSmall(5),rX#(x),rY#(y) : y=y+39
 DrawOption(8,rX#(x),rY#(y),"Music",softMusic(editChar)+"%") : DrawImage gIconSmall(6),rX#(x),rY#(y) : y=y+42 
 DrawOption(9,rX#(x),rY#(y),"Controversial",textYesNo$(softControversial(editChar)))
 DrawOption(10,rX#(x),rY#(565),"<<< BACK <<<","")  
 ;name entry
  y=210
  If MouseDown(1) And foc=1 And gotim>20 And keytim=0
    PlaySound sMenuBrowse : keytim=10 : FlushKeys()
    DrawOption(1,rX#(x),rY#(y),"Title","     ") : Flip
    Locate rX#(x)+10,rY#(y)-9 : Color 255,255,255
    SetFont font(7) 
    oldName$=softTitle$(editChar)
    softTitle$(editChar)=Left$(Input$(""),30) 
    If softTitle$(editChar)="" Then softTitle$(editChar)=oldName$
  EndIf
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect
 EndIf  
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer 
;Loader("Please Wait","Saving Changes")
SaveGameData(editChar)
screen=17
End Function

;///////////////////////////////////////////////////////
;----------------- RELATED FUNCTIONS -------------------
;///////////////////////////////////////////////////////

;SAVE GAME DATA
Function SaveGameData(cyc)
 file=WriteFile("Games/Data/Game"+Dig$(cyc,100)+".dat") 
   WriteString file,softTitle$(cyc)
   WriteInt file,softDated(cyc)
   WriteInt file,softGameplay(cyc)
   WriteInt file,softReplay(cyc)
   WriteInt file,soft2D(cyc)
   WriteInt file,soft3D(cyc)
   WriteInt file,softSound(cyc)
   WriteInt file,softMusic(cyc) 
   WriteInt file,softControversial(cyc)
 CloseFile(file)
End Function

;LOAD GAME DATA
Function LoadGameData()
 For cyc=1 To no_games 
  If FileType(CurrentDir$()+"Games/Data/Game"+Dig$(cyc,100)+".dat")=1
   file=ReadFile("Games/Data/Game"+Dig$(cyc,100)+".dat")
    softTitle$(cyc)=ReadString(file)
    softDated(cyc)=ReadInt(file)
    softGameplay(cyc)=ReadInt(file)
    softReplay(cyc)=ReadInt(file)
    soft2D(cyc)=ReadInt(file)
    soft3D(cyc)=ReadInt(file)
    softSound(cyc)=ReadInt(file)
    softMusic(cyc)=ReadInt(file)
    softControversial(cyc)=ReadInt(file)
   CloseFile(file)
  Else
   softTitle$(cyc)="Game #"+Dig$(cyc,10)
   softGameplay(cyc)=Rnd(10,90) : softReplay(cyc)=Rnd(10,90)
   soft2D(cyc)=Rnd(10,90) : soft3D(cyc)=Rnd(10,90)
   softSound(cyc)=Rnd(10,90) : softMusic(cyc)=Rnd(10,90)
   softDated(cyc)=0 : softControversial(cyc)=0 
  EndIf
 Next
End Function

;LOAD SOFTWARE BOX ART
Function LoadGameBox(cyc)
 softBox(cyc)=LoadImage("Games/Box Art/Box"+Dig$(cyc,100)+".JPG")
 ResizeImage softBox(cyc),100,145
 MaskImage softBox(cyc),255,0,255
End Function

;LOAD SOFTWARE SCREENSHOT
Function LoadGameScreen(cyc)
 softScreen(cyc)=LoadImage("Games/Screenshots/Screen"+Dig$(cyc,100)+".JPG")
 If softScreen(cyc)=0 Then softScreen(cyc)=LoadImage("Games/Box Art/Box"+Dig$(cyc,100)+".JPG")
 ResizeImage softScreen(cyc),230,145
 MaskImage softScreen(cyc),255,0,255
End Function

;LOAD PROJECT IMAGES
Function LoadProjectImages()
 Loader("Please Wait","Restoring Projects")
 For cyc=0 To no_projects
  Loader("","")
  LoadProjectBox(cyc)
  LoadProjectScreen(cyc)
 Next
End Function

;LOAD PROJECT BOX ART
Function LoadProjectBox(cyc)
 If projIdentified(cyc)>0 Or cyc=0
  ;Loader("","")
  projBox(cyc)=LoadImage("Games/Box Art/Box"+Dig$(projIdentified(cyc),100)+".JPG")
  ResizeImage projBox(cyc),100,145
  MaskImage projBox(cyc),255,0,255
 EndIf
End Function

;LOAD PROJECT SCREENSHOT
Function LoadProjectScreen(cyc)
 ;Loader("","")
 If projIdentity(cyc)=>0
  projScreen(cyc)=LoadImage("Games/Screenshots/Screen"+Dig$(projIdentity(cyc),100)+".JPG")
  If projScreen(cyc)=0 Then projScreen(cyc)=LoadImage("Games/Box Art/Box"+Dig$(projIdentity(cyc),100)+".JPG")
 Else
  projScreen(cyc)=LoadImage("Games/Development/Screen"+Dig$(MakePositive#(projIdentity(cyc)),10)+".JPG")
 EndIf
 ResizeImage projScreen(cyc),230,145
 MaskImage projScreen(cyc),0,255,0
End Function

;RESET PROJECT SLOT
Function ResetProject(cyc)
 projTitle$(cyc)="Project #"+Dig$(cyc,10)
 projIdentity(cyc)=0 : projIdentified(cyc)=0
 projBox(cyc)=projBox(0) : projScreen(cyc)=projScreen(0)
 projFormat(cyc)=gamHardware : projLabel(cyc)=charFed(gamChar)
 projDate(cyc)=gamDate : projReleased(cyc)=0
 projReview(cyc)=0 : projSales(cyc)=0 : projPirated(cyc)=0
 projGameplay(cyc)=0 : projReplay(cyc)=0
 proj2D(cyc)=0 : proj3D(cyc)=0
 projSound(cyc)=0 : projMusic(cyc)=0 
 projOldGameplay(cyc)=projGameplay(cyc)
 projOldReplay(cyc)=projReplay(cyc)
 projOld2D(cyc)=proj2D(cyc)
 projOld3D(cyc)=proj3D(cyc)
 projOldSound(cyc)=projSound(cyc)
 projOldMusic(cyc)=projMusic(cyc)
 For count=1 To 6
  projHiIncrease(cyc,count)=0
  projCredit(cyc,count)=0
  projLocked(cyc,count)=0
 Next
End Function

;IDENTIFY PROJECT
Function IdentifyProject(cyc)
 ;original work
 If projIdentified(cyc)=<0
  leader=0 : hi=0
  For count=1 To 100;no_games
   v=Rnd(1,no_games)
   matches=0
   If projGameplay(cyc)=>softGameplay(v)-5 And projGameplay(cyc)=<softGameplay(v)+5 Then matches=matches+2
   If projReplay(cyc)=>softReplay(v)-5 And projReplay(cyc)=<softReplay(v)+5 Then matches=matches+2
   If proj2D(cyc)=>soft2D(v)-5 And proj2D(cyc)=<soft2D(v)+5
    If soft3D(v)=>50 Then matches=matches+1 Else matches=matches+3
   EndIf
   If soft3D(v)>0 And proj3D(cyc)=>soft3D(v)-5 And proj3D(cyc)=<soft3D(v)+5
    If soft3D(v)<50 Then matches=matches+2 Else matches=matches+3
   EndIf
   If projSound(cyc)=>softSound(v)-5 And projSound(cyc)=<softSound(v)+5 Then matches=matches+1
   If projMusic(cyc)=>softMusic(v)-5 And projMusic(cyc)=<softMusic(v)+5 Then matches=matches+1
   For proj=1 To no_projects
    If proj<>cyc And projIdentity(proj)=v Then matches=0
   Next
   If projOldIdentity(cyc)>0
    If projGameplay(cyc)>projOldGameplay(cyc) And softGameplay(v)<softGameplay(projOldIdentity(cyc)) Then matches=0
    If projReplay(cyc)>projOldReplay(cyc) And softReplay(v)<softReplay(projOldIdentity(cyc)) Then matches=0
    If proj2D(cyc)>projOld2D(cyc) And soft2D(v)<soft2D(projOldIdentity(cyc)) Then matches=0
    If proj3D(cyc)>projOld3D(cyc) And soft3D(v)<soft3D(projOldIdentity(cyc)) Then matches=0
    If projSound(cyc)>projOldSound(cyc) And softSound(v)<softSound(projOldIdentity(cyc)) Then matches=0
    If projMusic(cyc)>projOldMusic(cyc) And softMusic(v)<softMusic(projOldIdentity(cyc)) Then matches=0
   EndIf
   If matches=>4 And matches>hi Then leader=v : hi=matches
  Next
 Else
  ;assignment
  leader=0 : matches=0
  v=projIdentified(cyc)
  If projGameplay(cyc)=>PercentOf#(softGameplay(v),75) Or projGameplay(cyc)=>hardGameplay(projFormat(cyc)) Then matches=matches+1
  If projReplay(cyc)=>PercentOf#(softReplay(v),75) Or projReplay(cyc)=>hardReplay(projFormat(cyc)) Then matches=matches+1
  If proj2D(cyc)=>PercentOf#(soft2D(v),75) Or proj2D(cyc)=>hard2D(projFormat(cyc)) Then matches=matches+1
  If proj3D(cyc)=>PercentOf#(soft3D(v),75) Or proj3D(cyc)=>hard3D(projFormat(cyc)) Then matches=matches+1
  If projSound(cyc)=>PercentOf#(softSound(v),75) Or projSound(cyc)=>hardSound(projFormat(cyc)) Then matches=matches+1
  If projMusic(cyc)=>PercentOf#(softMusic(v),75) Or projMusic(cyc)=>hardMusic(projFormat(cyc)) Then matches=matches+1
  If matches=>6 Then leader=v
 EndIf
 Return leader
End Function

;PORT PROJECT (to different format)
Function PortProject(source,format)
 no_projects=no_projects+1
 If no_projects>99 Then no_projects=99
 cyc=no_projects
 ResetProject(cyc)
 projFormat(cyc)=format : gamHardware=format
 projIdentity(cyc)=projIdentity(source) : projIdentified(cyc)=projIdentified(source)
 projTitle$(cyc)=softTitle$(projIdentity(cyc))
 If softDated(projIdentity(cyc))>0 Then projTitle$(cyc)=projTitle$(cyc)+" "+GetYear(gamDate)
 projBox(cyc)=projBox(source) : projScreen(cyc)=projScreen(source)
 projGameplay(cyc)=projGameplay(source)/2 : projReplay(cyc)=projReplay(source)/2
 proj2D(cyc)=proj2D(source)/2 : proj3D(cyc)=proj3D(source)/2
 projSound(cyc)=projSound(source)/2 : projMusic(cyc)=projMusic(source)/2
 For count=1 To 6
  projCredit(cyc,count)=projCredit(source,count)
 Next
 gamProject=cyc
End Function

;PORT GAME (begin making existing game)
Function PortGame(source)
 no_projects=no_projects+1
 If no_projects>99 Then no_projects=99
 cyc=no_projects
 ResetProject(cyc)
 projIdentified(cyc)=source
 projTitle$(cyc)=softTitle$(source)
 If softDated(source)>0 Then projTitle$(cyc)=projTitle$(cyc)+" "+GetYear(gamDate) 
 gamProject=cyc
End Function

;COUNT RELEASED PROJECTS
Function CountReleases() 
 value=0
 For v=1 To no_projects
  If projReleased(v)>0 Then value=value+1
 Next
 Return value
End Function

;DRAW PROJECT PROFILE
Function DrawProjectProfile(cyc,x,y,detail) ;1=full, 0=stats alone, -1=images+stats
 ;offset
 x=x+60
If detail<>0
 ;images
 If projIdentified(cyc)>0
  DrawImage projBox(cyc),x-315,y+74
  If projPirated(cyc)>0 Then DrawImage gPiracy,x-315,y+74
 Else
  DrawImage projBox(0),x-315,y+74
  DrawImage gFormat(hardFormat(projFormat(cyc))),x-315,y+74
 EndIf
 DrawImage projScreen(cyc),x-145,y+74
 If detail=1
  ;name
  SqueezeFont(projTitle$(cyc),200,20)
  OutlineStraight(projTitle$(cyc),x-15,y+11,0,0,0,255,255,255)
  spacer=StringWidth(projTitle$(cyc))
  SetFont font(3)
  OutlineStraight("("+hardTitle$(projFormat(cyc))+")",(x-15)+(spacer+8),y+11,0,0,0,200,200,200)
  ;sales
  If projReleased(cyc)>0 
   SetFont font(1)
   namer$=GetFigure$(projSales(cyc))+" sold since "+textMonth$(GetMonth(projReleased(cyc)))+" "+GetYear(projReleased(cyc))+" ("+projReview(cyc)+"%)"
   OutlineStraight(namer$,x-363,y+156,0,0,0,200,200,250)
  EndIf
  ;credits
  SetFont font(1)
  r=100 : g=100 : b=100
  If screen=50 Then r=175 : g=175 : b=175
  If projCredit(cyc,1)>0 Then OutlineStraight(charName$(projCredit(cyc,1)),(x+39)+(hardGameplay(projFormat(cyc))*2),y+32,0,0,0,r,g,b)
  If projCredit(cyc,2)>0 Then OutlineStraight(charName$(projCredit(cyc,2)),(x+39)+(hardReplay(projFormat(cyc))*2),y+53,0,0,0,r,g,b)
  If projCredit(cyc,3)>0 Then OutlineStraight(charName$(projCredit(cyc,3)),(x+39)+(hard2D(projFormat(cyc))*2),y+74,0,0,0,r,g,b)
  If projCredit(cyc,4)>0 Then OutlineStraight(charName$(projCredit(cyc,4)),(x+39)+(hard3D(projFormat(cyc))*2),y+95,0,0,0,r,g,b)
  If projCredit(cyc,5)>0 Then OutlineStraight(charName$(projCredit(cyc,5)),(x+39)+(hardSound(projFormat(cyc))*2),y+116,0,0,0,r,g,b)
  If projCredit(cyc,6)>0 Then OutlineStraight(charName$(projCredit(cyc,6)),(x+39)+(hardMusic(projFormat(cyc))*2),y+137,0,0,0,r,g,b)
 EndIf
EndIf
 ;meters
 DrawMeter(1,x-12,y+32,projGameplay(cyc),projOldGameplay(cyc),hardGameplay(projFormat(cyc)))
 DrawMeter(2,x-12,y+53,projReplay(cyc),projOldReplay(cyc),hardReplay(projFormat(cyc)))
 DrawMeter(3,x-12,y+74,proj2D(cyc),projOld2D(cyc),hard2D(projFormat(cyc)))
 DrawMeter(4,x-12,y+95,proj3D(cyc),projOld3D(cyc),hard3D(projFormat(cyc)))
 DrawMeter(5,x-12,y+116,projSound(cyc),projOldSound(cyc),hardSound(projFormat(cyc)))
 DrawMeter(6,x-12,y+137,projMusic(cyc),projOldMusic(cyc),hardMusic(projFormat(cyc)))
 ;hi scores
 If screen=50 And screenAgenda=4 And screenState=2
  SetFont font(2)
  If gamNewCredit=1 Then OutlineStraight("CREDIT!",(x+39)+(hardGameplay(projFormat(cyc))*2),y+32,0,0,0,255,Rnd(150,250),0)
  If gamNewCredit=2 Then OutlineStraight("CREDIT!",(x+39)+(hardReplay(projFormat(cyc))*2),y+53,0,0,0,255,Rnd(150,250),0)
  If gamNewCredit=3 Then OutlineStraight("CREDIT!",(x+39)+(hard2D(projFormat(cyc))*2),y+74,0,0,0,255,Rnd(150,250),0)
  If gamNewCredit=4 Then OutlineStraight("CREDIT!",(x+39)+(hard3D(projFormat(cyc))*2),y+95,0,0,0,255,Rnd(150,250),0)
  If gamNewCredit=5 Then OutlineStraight("CREDIT!",(x+39)+(hardSound(projFormat(cyc))*2),y+116,0,0,0,255,Rnd(150,250),0)
  If gamNewCredit=6 Then OutlineStraight("CREDIT!",(x+39)+(hardMusic(projFormat(cyc))*2),y+137,0,0,0,255,Rnd(150,250),0)
 EndIf
End Function

;DRAW SOFTWARE PROFILE
Function DrawGameProfile(cyc,x,y,detail) ;1=full, 0=stats alone, -1=images+stats
 ;offset
 x=x+60
If detail<>0
 ;images
 If softBox(cyc)>0
  DrawImage softBox(cyc),x-315,y+74
 Else
  DrawImage softBox(0),x-315,y+74
 EndIf
 If softScreen(cyc)>0
  DrawImage softScreen(cyc),x-145,y+74
 Else
  DrawImage softScreen(0),x-145,y+74
 EndIf
 If detail=1
  ;name
  SqueezeFont(softTitle$(cyc),200,20)
  OutlineStraight(softTitle$(cyc),x-15,y+11,0,0,0,255,255,255)
  spacer=StringWidth(softTitle$(cyc))
  If screen=3
   SetFont font(3)
   OutlineStraight("(PC)",(x-15)+(spacer+8),y+11,0,0,0,200,200,200)
   ;credits
   SetFont font(1)
   OutlineStraight("Mat Dickie",(x+39)+(99*2),y+32,0,0,0,100,100,100)
   OutlineStraight("Mat Dickie",(x+39)+(99*2),y+53,0,0,0,100,100,100)
   OutlineStraight("Mat Dickie",(x+39)+(99*2),y+74,0,0,0,100,100,100)
   OutlineStraight("Mat Dickie",(x+39)+(99*2),y+95,0,0,0,100,100,100)
   OutlineStraight("Various",(x+39)+(99*2),y+116,0,0,0,100,100,100)
   OutlineStraight("Various",(x+39)+(99*2),y+137,0,0,0,100,100,100)
  EndIf
 EndIf
EndIf
 ;meters
 DrawMeter(1,x-12,y+32,softGameplay(cyc),softGameplay(cyc),99)
 DrawMeter(2,x-12,y+53,softReplay(cyc),softReplay(cyc),99)
 DrawMeter(3,x-12,y+74,soft2D(cyc),soft2D(cyc),99)
 DrawMeter(4,x-12,y+95,soft3D(cyc),soft3D(cyc),99)
 DrawMeter(5,x-12,y+116,softSound(cyc),softSound(cyc),99)
 DrawMeter(6,x-12,y+137,softMusic(cyc),softMusic(cyc),99)
End Function