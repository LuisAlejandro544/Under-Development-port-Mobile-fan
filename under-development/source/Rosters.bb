;//////////////////////////////////////////////////////////////////////////////
;--------------------------- 10. SLOT SELECTION -------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectSlot()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
game=1
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
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If MouseDown(1)
	  If foc=>1 And foc=<4 Then go=1
	  If foc=5 And game=1 Then go=1
	  If foc=6 Then go=-1
	  ;block access
	  If go=1 And game=0 And foc=>2 And foc=<4
       If slotActive(foc-1)=0 Then BlockAccess()
      EndIf 
      ;resetting process
      ;If foc=5 And game=0
       ;If confirmer=0 Then confirmer=1 : PlaySound sMenuGo : keytim=10
	   ;If confirmer=1 And keytim=0
	    ;Loader("Please Wait","Restoring Backup Universe")
	    ;PlaySound sMenuBrowse : keytim=10
	    ;LoadWorld(-1)
	    ;LoadChars(-1)
	    ;LoadPhotos(-1)
	    ;Loader("Please Wait","Replacing Current Universe")
	    ;SaveWorld(0)
	    ;SaveChars(0)
	    ;SavePhotos(0)	    
	    ;PlaySound sTrash : confirmer=0
       ;EndIf
      ;EndIf
	 EndIf
	 ;editor cheat
	 If KeyDown(18) And foc=>2 And foc=<4 Then go=2
	EndIf
	;manage music
	ManageMusic(0) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;delete records
	 If (KeyDown(14) Or KeyDown(211)) And foc=>2 And foc=<4
	  If slotActive(foc-1)>0
	   PlaySound sTrash : keytim=10
	   slotActive(foc-1)=0
	  EndIf
	 EndIf   
	EndIf  
    ;reset confirmer
	If foc<>4 Then confirmer=0
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 DrawTitleScreen()
 ;options
 foc=0
 If game=1 Then y=270 Else y=330
 If game=0 Then DrawOption(1,rX#(400),rY#(y)-60,"DEFAULT UNIVERSE","")
 For count=1 To 3
  If game=0
   If slotActive(count)>0 Then namer$="CAREER "+count+"'S UNIVERSE" Else namer$="EMPTY"
  EndIf
  If game=1
   If slotActive(count)>0 Then namer$=count+". "+slotName$(count) Else namer$="NEW CAREER"
  EndIf
  DrawOption(count+1,rX#(400),rY#(y),namer$,"")
  If slotActive(count)>0
   SetFont font(2)
   namer$="("+textMonth$(GetMonth(slotDate(count)))+" "+GetYear(slotDate(count))+")"
   Outline(namer$,rX#(400),rY#(y)+17,0,0,0,ColorRed(),ColorGreen(),ColorBlue())
  EndIf
  If slotActive(count)>0 Then DrawImage gFormat(2),rX#(400)-116,rY#(y)
  y=y+50
 Next
 y=y+10
 ;If game=0 And screenAgenda=1
  ;If confirmer=1 Then namer$="ARE YOU SURE?" Else namer$="RESET UNIVERSE"
  ;DrawOption(5,rX#(400),rY#(y),namer$,"") : y=y+60
 ;EndIf 
 If game=1 
  DrawOption(5,rX#(400),rY#(y),"HALL OF FAME","")
  DrawImage gCrown,rX#(400),rY#(y)-17 : y=y+60
 EndIf
 DrawOption(6,rX#(400),rY#(y),"<<< BACK <<<","")  
 ;cursor
 If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect  
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go>0 Then PlaySound sMenuGo Else PlaySound sMenuBack
If go=-1 Then screen=1
If go=1 And foc=5 Then screen=26
;access slot
If go=1 And foc=>1 And foc=<4
 If game=1 And slotActive(foc-1)=0
  targetSlot=foc-1
  screen=2 : screenAgenda=5
   ;Loader("Please Wait","Generating Career")
   ;SwitchSlot(0)
   ;slot=foc-1
   ;gamInherit=0
   ;GenerateCareer()
   ;slotActive(slot)=1
   ;SaveUniverse()
   ;SaveProgress(slot)
   ;SaveWorld(slot)
   ;SaveChars(slot)
   ;SavePhotos(slot)  
   ;editChar=gamChar
   ;screen=51 : screenAgenda=5
 Else
  If slot<>foc-1 Then Loader("Please Wait","Restoring Data")
  SwitchSlot(foc-1)
  If game=1 Then screen=20 Else screen=11
 EndIf
EndIf
;editing
If go=2
 If slot<>foc-1 Then Loader("Please Wait","Restoring Data")
 SwitchSlot(foc-1)
 game=0 : screen=11 : screenAgenda=1
EndIf
End Function

;//////////////////////////////////////////////////////////////////////////////
;-------------------------- 11. ROSTER SELECTION ------------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectRoster()
;editor locks
For count=1 To no_feds
 fedLocked(count)=0
Next
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
	flashTim=flashTim+1
	If flashTim>30 Then flashTim=0  
	
	;PORTAL
    gotim=gotim+1
	If gotim>20 And keytim=0
	 ;leave
	 If KeyDown(1) Then go=-1
	 ;proceed
	 If MouseDown(1)
	  If foc=11 Then go=-1 Else go=1
	  ;block access
	  If go=1 And screenAgenda=1 And foc=>1 And foc=<no_feds
       If fedLocked(foc) Then BlockAccess()
      EndIf  
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;CONFIGURATION 
	If gotim>20 And keytim=0
	 ;edit properties
     If screenAgenda=1 And foc=>1 And foc=<no_feds 
      If fedLocked(foc)=0
       If KeyDown(16) Then fedBank(foc)=fedBank(foc)-10000 : PlaySound sMenuBrowse : keytim=3
	   If KeyDown(17) Then fedBank(foc)=fedBank(foc)+10000 : PlaySound sMenuBrowse : keytim=3
	   If KeyDown(30) Then fedPopularity(foc)=fedPopularity(foc)-1 : PlaySound sMenuBrowse : keytim=3
	   If KeyDown(31) Then fedPopularity(foc)=fedPopularity(foc)+1 : PlaySound sMenuBrowse : keytim=3
	   If KeyDown(44) Then fedReputation(foc)=fedReputation(foc)-1 : PlaySound sMenuBrowse : keytim=3
	   If KeyDown(45) Then fedReputation(foc)=fedReputation(foc)+1 : PlaySound sMenuBrowse : keytim=3
	   CheckFedLimits(foc)
	  EndIf
	 EndIf 
	 ;generate contracts
	 If KeyDown(56) And KeyDown(46)
	  PlaySound sPaper : keytim=10
	  For char=1 To no_chars
	   GenerateContract(char)
	  Next
	 EndIf
	 ;unlock rosters cheat
	 If screenAgenda=1 And KeyDown(25) And KeyDown(3)
	  If MouseX()>rX#(400)-100 And MouseX()<rX#(400)+100 And MouseY()>rY#(60)-50 And MouseY()<rY#(60)+50
	   PlaySound sCheer : keytim=10
	   For count=1 To no_feds
	    fedLocked(count)=0
	   Next
	  EndIf
	 EndIf 
	 ;reset roster locks
	 If screenAgenda=1 And KeyDown(29) And KeyDown(19)
	  PlaySound sTrash : keytim=10
	  For count=1 To no_feds
	   fedLocked(count)=1
	  Next
	 EndIf  
	EndIf   
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;labels
 nameX=0 : nameY=0
 x=rX#(400)-235 : y=rY#(140)
 For cyc=1 To no_feds 
  If MouseX()=>x-80 And MouseX()=<x+80 And MouseY()=>y-90 And MouseY()=<y Then foc=cyc
  DrawImage gLabel(cyc),x,y-60
  DrawOption(cyc,x,y,fedName$(cyc),"") 
  If fedBooker(cyc)>0 Then DrawImage charPhoto(fedBooker(cyc)),x-80,y-65
  If foc=cyc Then nameX=x : nameY=y
  x=x+235
  If x>rX#(400)+400 Then x=rX#(400)-235 : y=y+rY#(155)
 Next
 DrawOption(10,rX#(400),rY#(510),"ALL CHARACTERS","")
 ;options
 If screenAgenda=7 Then namer$="<<< SAVE <<<" Else namer$="<<< BACK <<<"
 DrawOption(11,rX#(400),rY#(565),namer$,"")  
 ;name changes
 If KeyDown(14) And screenAgenda=1 And foc=>1 And foc=<no_feds And gotim>40 And keytim=0
  If fedLocked(foc)=0
   PlaySound sMenuBrowse : keytim=10 : FlushKeys()
   DrawOption(-1,nameX,nameY,"","") : Flip
   Locate nameX-85,nameY-9 : Color 255,255,255
   SetFont font(7) 
   oldName$=fedName$(foc)
   fedName$(foc)=Left$(Input$(""),30) 
   If fedName$(foc)="" Then fedName$(foc)=oldName$
  EndIf
 EndIf
 ;roster preview
 If KeyDown(57) Or MouseDown(2)
  If foc=>1 And foc=<9
   x=rX#(400)-300
   If fedSize(foc)>1 Then spacing=600/(fedSize(foc)-1)
   If spacing>30 Then spacing=30
   For count=1 To fedSize(foc)
    DrawImage charPhoto(fedRoster(foc,count)),x,rY#(500)
    x=x+spacing 
   Next
  EndIf
 EndIf
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 Then PlaySound sMenuSelect  
 EndIf
 DrawImage gCursor,MouseX(),MouseY()
 If screenAgenda=7 Then DrawImage charPhoto(editChar),MouseX()+40,MouseY()+25

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=1 Or screenAgenda=7 Then PlaySound sMenuGo Else PlaySound sMenuBack 
editFoc=0 : editScroll=0 : editFilter=0 : optGrid=1
If go=1
 If foc=>1 And foc=<no_feds Then fed=foc Else fed=0
EndIf
If screenAgenda=<1 ;editing
 If go=-1 Then screen=1
 If go=1 Then screen=12 
EndIf
If screenAgenda=3 ;recruiting
 If go=-1 Then screen=15
 If go=1 Then screen=12
EndIf
If screenAgenda=7 ;setting relationships
 If go=-1 Then screen=51 : screenAgenda=1
 If go=1 Then screen=12
EndIf
End Function

;//////////////////////////////////////////////////////////////////////////////
;------------------------- 12. CHARACTER SELECTION ----------------------------
;//////////////////////////////////////////////////////////////////////////////
Function SelectCharacter()
;reset ranking criteria
;optGrid=1 : editFilter=0
GetRankings(editFilter)
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
cloner=0 : scroll=editScroll
foc=editFoc : oldfoc=foc : lastfoc=foc
If lastfoc=0 Then lastfoc=1
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
	 ;leave / cancel switching
	 If KeyDown(1) And cloner=0 Then go=-1 
	 If (KeyDown(1) Or MouseDown(1)) And cloner>0 Then cloner=0 : PlaySound sMenuBack : keytim=10
	 ;proceed
	 If MouseDown(1) And foc=999 Then go=-1
	 If MouseDown(1) And foc=>1 And foc=<no_boxes And subFoc=-1 And screenAgenda<>7
	  If MouseY()=>rY#(215)-15 And MouseY()=<rY#(515)+15 Then editFoc=foc : editScroll=scroll : go=1
	  If screenAgenda=3 And (fedRank(fed,foc)=gamChar Or charTeam(fedRank(fed,foc))=gamChar) Then BlockAccess()
	 EndIf
	 ;switch display
	 If MouseDown(2) Or KeyDown(15)
	  PlaySound sMenuBrowse : keytim=10
	  If optGrid=1 Then optGrid=0 Else optGrid=1
	  If optGrid=1 Then editFilter=0
	  scroll=0
	 EndIf
	EndIf
	;manage music
	ManageMusic(0)
	
	;ASSESS ROSTER
	;count character slots
	no_boxes=0 
    For count=1 To no_chars
     If fedRank(fed,count)>0 Then no_boxes=no_boxes+1
    Next
    ;add one for editing
    If screenAgenda=1 And fed>0 And no_chars<optCharLim And fedSize(fed)<optRosterLim
     no_boxes=fedSize(fed)+1
     charName$(no_chars+1)="~ NEW CHARACTER ~"
     fedRoster(fed,no_boxes)=no_chars+1
     fedRank(fed,no_boxes)=no_chars+1
     charPhoto(no_chars+1)=charPhoto(0)
    EndIf

	;CONFIGURATION
	If gotim>20 And keytim=0 And foc=>1 And foc=<no_boxes
	 ;FIDDLES
	 char=fedRank(fed,foc) 
	 If screenAgenda=1 And foc>0 And char=<no_chars
	  ;switch rosters
	  If fedSize(charFed(char))>1
	   For count=1 To 9
	    If KeyDown(count+1) And fedSize(count)<optRosterLim And charFed(char)<>count
	     If char<>gamChar Or slot=0
	      PlaySound sSwing : keytim=10
	      MoveChar(char,count)
	      GenerateContract(char)
	      editFilter=0
          GetRankings(editFilter)
	      foc=0
	     EndIf
	    EndIf
	   Next 
	  EndIf
	  ;assign booker
	  If KeyDown(48) And fedBooker(charFed(char))<>char
	   If char<>gamChar Or slot=0 Then fedBooker(charFed(char))=char : PlaySound sPaper : keytim=10
	  EndIf
	  ;generate stats
	  If KeyDown(56) And KeyDown(31)
	   randy=Rnd(2,19) : charDesign(char)=randy*5
	   randy=Rnd(2,19) : charProgram(char)=randy*5
 	   randy=Rnd(2,19) : char2D(char)=randy*5
 	   randy=Rnd(2,19) : char3D(char)=randy*5
 	   randy=Rnd(2,19) : charSound(char)=randy*5
 	   randy=Rnd(2,19) : charMusic(char)=randy*5
       randy=Rnd(4,19) : charRep(char)=randy*5
 	   CheckLimits(char)
 	   GenerateContract(char)
	  EndIf
	  ;generate contract
	  If KeyDown(56) And KeyDown(46)
	   PlaySound sPaper : keytim=10
	   GenerateContract(char)
	  EndIf
	 EndIf
	 ;ACTIVATIONS
	 ;set relationship
	 If MouseDown(1) And game=0 And screenAgenda=7
	  char=fedRank(fed,foc)
	  If foc>0 And char<>no_chars+1 And subFoc=-1
	   If char=editChar
	    PlaySound sVoid : keytim=15
	   Else
	    PlaySound sMenuBrowse : keytim=8
	    charRelationship(editChar,char)=charRelationship(editChar,char)+1
	    If charRelationship(editChar,char)>1 Then charRelationship(editChar,char)=-1
	    charRelationship(char,editChar)=charRelationship(editChar,char)
	   EndIf
	  EndIf
	 EndIf
	 ;change rank criteria
	 If MouseDown(1) And foc>0 And subFoc=>0 And subFoc=<10
	  PlaySound sMenuBrowse : keytim=10
	  editFilter=subFoc : scroll=0 
	  GetRankings(editFilter)
	 EndIf 
	 ;CLONING/SWITCHING
	 char=fedRank(fed,foc) 
	 If MouseDown(2) And foc=9999 ;blocked!
	  ;initiate
	  If foc>0 And char<>no_chars+1 And cloner=0 Then cloner=foc : PlaySound sMenuGo : keytim=10
	  ;cancel
	  If cloner>0 And cloner=foc And keytim=0 Then cloner=0 : PlaySound sMenuBack : keytim=10
	  ;execute
	  If cloner>0 And cloner<>foc And keytim=0
	   PlaySound sMenuGo : keytim=10
       If fed>0 And foc=fedSize(fed)+1 ;clone to new slot
 	    fedSize(fed)=fedSize(fed)+1 : no_chars=no_chars+1
        If no_chars>optCharLim Then no_chars=optCharLim
        fedRoster(fed,foc)=no_chars : fedRank(fed,foc)=no_chars
        CopyChar(fedRank(fed,cloner),no_chars)
        charPhoto(no_chars)=CopyImage(charPhoto(fedRank(fed,cloner))) 
        SaveImage(charPhoto(no_chars),"Data/Slot0"+slot+"/Portraits/Photo"+Dig$(no_chars,100)+".bmp")
        charFed(no_chars)=charFed(fedRank(fed,cloner))    
       Else
        oldSlot=fedRank(fed,foc) ;swap existing
        fedRank(fed,foc)=fedRank(fed,cloner)
        fedRank(fed,cloner)=oldSlot
        If optGrid=1 And charFed(fedRank(fed,foc))=charFed(fedRank(fed,cloner))
         f=charFed(fedRank(fed,cloner))
         oldSlot=fedRoster(f,foc)
         fedRoster(f,foc)=fedRoster(f,cloner)
         fedRoster(f,cloner)=oldSlot
        EndIf 
       EndIf
       cloner=0
	  EndIf
	 EndIf
	EndIf
	
	;SCROLLING
	If optGrid=1
	 ;grid display
	 no_displays=(Int((rY#(515)-rY#(215))/38)+1)*3
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   If fed=0 Then scrollSpeed=2 Else scrollSpeed=4
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=scrollSpeed
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
	Else
	 ;list display
	 no_displays=Int((rY#(515)-rY#(215))/38)+1
	 If no_boxes>no_displays
	  If MouseDown(1) And keytim=0 
	   If fed=0 Then scrollSpeed=2 Else scrollSpeed=4
	   If subFoc=20 Then scroll=scroll+38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	   If subFoc=21 Then scroll=scroll-38 : PlaySound sMenuBrowse : keytim=scrollSpeed
	  EndIf
	  If scroll>0 Then scroll=0
	  If scroll<-((no_boxes-no_displays)*38) Then scroll=-((no_boxes-no_displays)*38)
	  If foc=>1 And foc=<no_boxes ;And foc<>oldfoc
	   If foc>no_displays And scroll>-((foc-no_displays)*38) Then scroll=-((foc-no_displays)*38)
	   If scroll<-((foc-1)*38) Then scroll=-((foc-1)*38) 
	  EndIf
     EndIf 
	EndIf
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;profile
 If lastfoc=>1 And lastfoc=<no_boxes And fedRank(fed,lastfoc)>0 And fedRank(fed,lastfoc)=<no_chars
  DrawCharProfile(fedRank(fed,lastfoc),rX#(400),rY#(20),1)
 EndIf
 ;scrollers
 resetSubFoc=1
 If optGrid=0 And MouseX()=>rX#(400)-19 And MouseX()=<rX#(400)+280 Then resetSubFoc=0
 If resetSubFoc=1 Then subFoc=-1
 If optGrid=1 Then indent=345 Else indent=330
 If no_boxes>no_displays 
  If scroll<0
   If optGrid=1 Then y=rY#(210)+58 Else y=rY#(215)+58
   If MouseY()>y-72 And MouseY()<y+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=20
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=20
   EndIf
   If subFoc=20
    DrawImage gScroll(3),rX#(400)-indent,y
    DrawImage gScroll(3),rX#(400)+indent,y
   Else
    DrawImage gScroll(1),rX#(400)-indent,y
    DrawImage gScroll(1),rX#(400)+indent,y
   EndIf
  EndIf
  excess=Int(((no_boxes-no_displays)-1)/3)+1
  If (optGrid=0 And scroll>-((no_boxes-no_displays)*38)) Or (optGrid=1 And scroll>-(excess*38))
   If optGrid=1 Then y=rY#(520)-58 Else y=rY#(515)-58
   If MouseY()>y-72 And MouseY()<y+73
    If MouseX()>(rX#(400)-indent)-13 And MouseX()<(rX#(400)-indent)+16 Then subFoc=21
    If MouseX()>(rX#(400)+indent)-13 And MouseX()<(rX#(400)+indent)+16 Then subFoc=21
   EndIf
   If subFoc=21
    DrawImage gScroll(4),rX#(400)-indent,y
    DrawImage gScroll(4),rX#(400)+indent,y
   Else
    DrawImage gScroll(2),rX#(400)-indent,y
    DrawImage gScroll(2),rX#(400)+indent,y
   EndIf
  EndIf
 EndIf
 ;CHARACTER BOXES
 foc=0
 If optGrid=0 Then x=rX#(400)-183 : listY=rY#(215)
 If optGrid=1 Then x=rX#(400)-205 : listY=rY#(210)
 sourceX=0 : sourceY=0
 targetX=0 : targetY=0
 For rank=1 To no_boxes
  char=fedRank(fed,rank)
  y=listY+scroll
  If char>0 And y=>rY#(210) And y=<rY#(525)
   ;mouse hotspots
   If optGrid=1
    If MouseX()=>x-101 And MouseX()=<x+101 And MouseY()=>y-15 And MouseY()=<y+17 Then foc=rank
   Else
    If MouseX()=>x-107 And MouseX()=<x+476 And MouseY()=>y-15 And MouseY()=<y+17
     foc=rank
     statX=x+108
     For count=1 To 7
      If MouseX()=>statX-12 And MouseX()=<statX+15 Then subFoc=count
      statX=statX+28
     Next
     If MouseX()=>x+294 And MouseX()=<x+348 Then subFoc=8
     If MouseX()=>x+350 And MouseX()=<x+404 Then subFoc=9
     If MouseX()=>x+406 And MouseX()=<x+476 Then subFoc=10
    EndIf 
   EndIf
   ;name box
   If optGrid=1 Then nameX=x Else nameX=x-8
   If foc=rank And subFoc<20 Then DrawImage gMenu(1),nameX,y Else DrawImage gMenu(2),nameX,y 
   DrawImageRect charPhoto(char),nameX-60,(y+64)-14,21,(64-PortraitHead(char))-14,34,29
   If char=fedBooker(charFed(char)) Then DrawImage gCrown,nameX,y-15
   SqueezeFont(charName$(char),150,18)
   If foc=rank And subFoc<20
    r=255 : g=240 : b=0 
    If screenAgenda=7 And charRelationship(editChar,char)>0 Then r=0 : g=255 : b=0
    If screenAgenda=7 And charRelationship(editChar,char)<0 Then r=225 : g=0 : b=0
   Else
    r=175 : g=175 : b=175 
    If screenAgenda=7 And charRelationship(editChar,char)>0 Then r=100 : g=150 : b=100
    If screenAgenda=7 And charRelationship(editChar,char)<0 Then r=150 : g=100 : b=100 
   EndIf
   Outline(charName$(char),nameX+10,y+1,0,0,0,r,g,b)
   ;statistics
   If optGrid=0 And char=<no_chars
    statX=x+108
    For count=1 To 7
     DrawStatBox(rank,char,count,statX,y)
     statX=statX+28
    Next
   EndIf
   ;contract
   If optGrid=0 And char=<no_chars
    SetFont fontNews(1)
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>8) Or subFoc=>20 Then Color 125,125,125 Else Color 255,255,255
    Rect x+293,y-16,53,32,1
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>8) Or subFoc=>20 Then Color 200,200,200 Else Color 255,240,100
    Rect x+293,y-16,53,32,0
    Outline(charAge(char)+"yrs",x+320,y,0,0,0,0,0,0)
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>9) Or subFoc=>20 Then Color 125,125,125 Else Color 255,255,255
    Rect x+349,y-16,53,32,1
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>9) Or subFoc=>20 Then Color 200,200,200 Else Color 255,240,100
    Rect x+349,y-16,53,32,0
    ;namer$=charContract(char)+" "+textPeriod$(optTime)+"s"
    If optTime=1 Then namer$=charContract(char)+"wks" Else namer$=charContract(char)+"mths"
    Outline(namer$,x+376,y,0,0,0,0,0,0)
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>10) Or subFoc=>20 Then Color 125,125,125 Else Color 255,255,255
    Rect x+405,y-16,69,32,1
    If foc<>rank Or (subFoc=>1 And subFoc=<10 And subFoc<>10) Or subFoc=>20 Then Color 200,200,200 Else Color 255,240,100
    Rect x+405,y-16,69,32,0
    SetFont fontNews(2) 
    Outline("$"+GetFigure$(charSalary(char)),x+440,y,0,0,0,0,0,0)
   EndIf
   ;cloning data
   If cloner=rank Then sourceX=x : sourceY=y 
   If foc=rank And subFoc<20 
    targetX=x : targetY=y 
    SetFont font(1)
    ;If cloner>0 And cloner<>rank And screenAgenda=1 Then Outline("RIGHT CLICK TO CLONE",x,y+12,0,0,0,255,240,100)
    If cloner>0 And cloner<>rank Then Outline("RIGHT CLICK TO SWITCH",x,y+13,0,0,0,255,240,100)
    If cloner>0 And cloner=rank Then Outline("RIGHT CLICK TO CANCEL",x,y+13,0,0,0,255,240,100)
   EndIf
  EndIf
  ;increment
  If optGrid=1
   x=x+205;189
   If x>rX#(400)+300 Then x=rX#(400)-205 : listY=listY+38
  Else 
   listY=listY+38
  EndIf
 Next
 ;plot clone line
 If cloner>0 And foc<>cloner And targetY>0 
  If optGrid=1
   DrawLine(sourceX,sourceY,targetX,targetY,255,240,100)
  Else
   DrawLine(sourceX,sourceY,sourceX-115,GetCentre#(sourceY,targetY),255,240,100)
   DrawLine(sourceX-115,GetCentre#(sourceY,targetY),targetX,targetY,255,240,100)
  EndIf
 EndIf
 ;stat headers
 If optGrid=0 And no_boxes>0
  x=(rX#(400)-185)+109 : y=rY#(187)
  For stat=1 To 7
   DrawImage gIconSmall(stat),x,y 
   x=x+28
  Next
 EndIf
 ;empty message
 If no_boxes=0
  SetFont font(6)
  Outline("This pool doesn't contain any characters!",rX#(400),rY#(285),0,0,0,255,255,255)
  Outline("Please exit and choose another...",rX#(400),rY#(315),0,0,0,255,255,255)
 EndIf
 ;option
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
 If screenAgenda=7 Then DrawImage charPhoto(editChar),MouseX()+40,MouseY()+25

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
If go=-1 Then PlaySound sMenuBack Else PlaySound sMenuGo
editFoc=foc : editScroll=scroll
If screenAgenda=<1 ;editing
 If go=-1 Then screen=11
 If go=1 Then editChar=fedRank(fed,foc) : editFed=fed : screen=51
EndIf
If screenAgenda=3 ;recruiting
 If go=-1 Then screen=11
 If go=1 Then negChar=fedRank(fed,foc) : negTopic=1 : screen=54
EndIf
If screenAgenda=7 ;setting relationships
 If go=-1 Then screen=11
 If go=1 Then screen=11
EndIf
End Function

;////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS -----------------------
;////////////////////////////////////////////////////////////////

;CHECK ROSTER STAT LIMITS
Function CheckFedLimits(cyc)
 ;standard limits
 If fedPopularity(cyc)<30 Then fedPopularity(cyc)=30 
 If fedPopularity(cyc)>99 Then fedPopularity(cyc)=99
 If fedReputation(cyc)<30 Then fedReputation(cyc)=30
 If fedReputation(cyc)>99 Then fedReputation(cyc)=99
 ;new limits
 If fedNewPopularity(cyc)>99 Then fedNewPopularity(cyc)=99
 If fedNewPopularity(cyc)<30 Then fedNewPopularity(cyc)=30
 If fedNewReputation(cyc)>99 Then fedNewReputation(cyc)=99
 If fedNewReputation(cyc)<30 Then fedNewReputation(cyc)=30
End Function

;AVERAGE CHARACTER STATS
Function AverageStats(char,rep)
 If rep=1
  value=charDesign(char)+charProgram(char)+char2D(char)+char3D(char)+charSound(char)+charMusic(char)+charRep(char)
  value=value/7
 Else
  value=charDesign(char)+charProgram(char)+char2D(char)+char3D(char)+charSound(char)+charMusic(char)
  value=value/6
 EndIf 
 Return value
End Function

;MOVE INDIVIDUAL CHARACTER
Function MoveChar(char,target)
 If charFed(char)<>target And fedSize(target)<50;optRosterLim
  ;queue reactions
  If game=1 Then FindTradeReactions(char,target) 
  ;find spot in current roster
  source=charFed(char)
  spot=0
  For count=1 To fedSize(source)
   If spot=0 And fedRoster(source,count)=char Then spot=count
  Next
  ;delete from source roster
  For count=spot To fedSize(source)-1
   fedRoster(source,count)=fedRoster(source,count+1)
  Next
  fedSize(source)=fedSize(source)-1
  ;add to target roster
  fedSize(target)=fedSize(target)+1
  fedRoster(target,fedSize(target))=char
  charFed(char)=target
  ;consistent t-shirts
  If charBody(char)=>22 And charBody(char)=<29 And target<no_feds
   charBody(char)=21+target
   If charBody(char)=24 Or charBody(char)=25 Or charBody(char)=28 Or charBody(char)=29
    If (charArms(char)=>8 And charArms(char)=<10) Or charArms(char)=43 Then charArms(char)=11
    If charArms(char)=41 Then charArms(char)=42 
   Else
    If charArms(char)=>11 And charArms(char)=<17 Then charArms(char)=8    
    If charArms(char)=42 Then charArms(char)=41
   EndIf
  EndIf
  ;new contract
  If game=0 Then GenerateContract(char)
  ;negate career responsibilities
  If game=1 And char=gamChar
   gamDeskPosition=Rnd(1,6)
   gamAgreement(1)=0
   gamMission=0
  EndIf
 EndIf
End Function

;MOVE WHOLE TEAM
Function MoveTeam(team,target)
 For count=1 To optTeamSize
  char=charRoster(team,count)
  If char>0 And charFed(char)<>target
   randy=Rnd(0,1)
   If (randy=0 Or char=team) And fedSize(target)<50;optRosterLim
    ;execute move
    MoveChar(char,target)
   Else 
    ;leave behind
    If char<>team Then LeaveTeam(char)
   EndIf
  EndIf
 Next
End Function

;JOIN TEAM
Function JoinTeam(char,team)
 If TeamSize(team)<optTeamSize Or char=team
  ;leave current team
  LeaveTeam(char)
  ;switch rosters
  If charFed(char)<>charFed(team) Then MoveChar(char,charFed(team))
  ;add to new team
  If FindInTeam(char,team)=0
   If team=gamChar And CountDesks()=<TeamSize(gamChar)
    For cyc=1 To optTeamSize
     If gamDesk(cyc)=0 Then gamDesk(cyc)=1 : Exit
    Next
   EndIf
   For cyc=1 To optTeamSize
    If charRoster(team,cyc)=0 And (gamDesk(cyc)<>0 Or team<>gamChar) Then charRoster(team,cyc)=char : Exit
   Next
  EndIf
  charTeam(char)=team
  ;stimulate host
  charTeam(team)=team
 EndIf
End Function

;LEAVE TEAM
Function LeaveTeam(char)
 team=charTeam(char)
 If team>0
  If team<>char
   For count=1 To optTeamSize
    If charRoster(team,count)=char Then charRoster(team,count)=0
   Next
  EndIf
  charTeam(char)=0
  charAgreement(char,2)=2
 EndIf
End Function

;COUNT TEAM SIZE
Function TeamSize(team)
 value=0
 If team>0
  For count=1 To optTeamSize
   If charRoster(team,count)>0 Then value=value+1
  Next
 EndIf
 Return value
End Function

;FIND CHARACTER IN TEAM
Function FindInTeam(char,team)
 value=0
 For count=1 To optTeamSize
  If charRoster(team,count)=char Then value=count
 Next
 Return value
End Function

;COUNT DESKS
Function CountDesks()
 value=0
 For count=1 To optTeamSize
  If gamDesk(count)<>0 Then value=value+1
 Next
 Return value
End Function

;CHECK WHETHER SLOT IS RE-USABLE
Function SlotUsable(checkSlot)
 value=1
 ;negate if files are missing
 If FileType(CurrentDir$()+"Data/Slot0"+checkSlot+"/Progress.dat")=0 Then value=0
 If FileType(CurrentDir$()+"Data/Slot0"+checkSlot+"/World.dat")=0 Then value=0
 If value=1
  For char=1 To no_chars
   If FileType(CurrentDir$()+"Data/Slot0"+checkSlot+"/Character"+Dig$(char,100)+".dat")=0 Then value=0
   If FileType(CurrentDir$()+"Data/Slot0"+checkSlot+"/Portraits/Photo"+Dig$(char,100)+".bmp")=0 Then value=0 
  Next
 EndIf
 Return value
End Function

;SWITCH SLOT
Function SwitchSlot(newSlot)
 oldSlot=slot
 If slot<>newSlot 
  ;save current
  SaveProgress(slot)
  SaveWorld(slot)
  SaveChars(slot)
  ;load new
  slot=newSlot
  LoadProgress(slot)
  LoadWorld(slot)
  LoadChars(slot)
  LoadPhotos(slot) 
  LoadProjectImages()
 EndIf
End Function

;JUMBLE ROSTERS
Function JumbleRosters()
      ;rosters
	  For fed=1 To no_feds
       fedSize(fed)=0
       For count=1 To optRosterLim
        fedRoster(fed,count)=0
       Next
      Next
      For char=1 To no_chars
       its=0
       Repeat
        satisfied=1 : its=its+1
        charFed(char)=Rnd(1,no_feds)
        If charFed(char)<no_feds And fedSize(charFed(char))=>18 Then satisfied=0
        randy=Rnd(0,1)
        If randy=0 And optJumble=1 And charFed(char)<no_feds And GetValue(char)<70 Then satisfied=0
        If optJumble=1 And charFed(char)=no_feds And GetValue(char)=>70 Then satisfied=0
       Until satisfied=1 Or its>100
       fedSize(charFed(char))=fedSize(charFed(char))+1
       fedRoster(charFed(char),fedSize(charFed(char)))=char
       If fedSize(charFed(char))=1 Then fedBooker(charFed(char))=char
       ;stats
       If optJumble=2 And char<>5
        If charFed(char)=no_feds Then limit=16 Else limit=19
        randy=Rnd(2,limit) : charDesign(char)=randy*5
 	    randy=Rnd(2,limit) : charProgram(char)=randy*5
 	    randy=Rnd(2,limit) : char2D(char)=randy*5
 	    randy=Rnd(2,limit) : char3D(char)=randy*5
 	    randy=Rnd(2,limit) : charSound(char)=randy*5
 	    randy=Rnd(2,limit) : charMusic(char)=randy*5
        If charFed(char)<no_feds Then randy=Rnd(6,19) : charRep(char)=randy*5
        If charFed(char)=no_feds Then randy=Rnd(4,10) : charRep(char)=randy*5
	    CheckLimits(char)
       EndIf
       GenerateContract(char)
      Next
End Function