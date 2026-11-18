;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: MEETINGS -----------------------
;//////////////////////////////////////////////////////////////////////////////

;----------------------------------------------------------------
;///////////////////// RISK COMPANY MEETINGS ////////////////////
;----------------------------------------------------------------
Function RiskCompanyMeetings()
 ;reset by default
 fed=charFed(gamChar) : negChar=fedBooker(fed)
 negTopic=0 : negSetting=0
 ;random suggestions
 If (gamMission=>1 And gamMission=<6) Or gamMission=14 Then mission=1 Else mission=0
 randy=Rnd(0,30)
 If randy=0 And gamAgreement(1)=0 And mission=0 And fed<no_feds Then negTopic=2 ;arcade port
 If randy=1 And gamAgreement(1)=0 And mission=0 Then negTopic=3 ;game commission
 If randy=2 And gamAgreement(1)=0 And mission=0 And CountReleases()>1 And CountMachines(1)>1 Then negTopic=4 ;port old project
 If projReleased(gamProject)=0 And gamDate>projDate(gamProject)+4 And projIdentified(gamProject)>0 And projIdentity(gamProject)>0
  If randy=3 Or (randy=4 And fedPolicy(fed)=8) Then negTopic=7 ;release game
 EndIf
 If randy=5 And gamRelease>0 And chartPromotion#(1)>25 And chartPromotion#(1)<100 And gamBank>0 Then negTopic=13 ;promote release 
 randy=Rnd(0,100)
 If randy=<1 And CountReleases()>0 Then negTopic=Rnd(18,19) ;sell movie/music rights
 ;SDK offer
 If gamBank>0 And fed<no_feds
  For v=1 To no_machines
   If hardReleased(v)>0 And hardPromotion#(v)>-50 And hardOwned(v)=0
    If fedName$(fed)=hardBrand$(v)
     randy=Rnd(0,50)
     If randy=<1 Or (randy=<20 And hardReleased(v)=gamDate) Then negVariable=v : negTopic=11
    Else
     randy=Rnd(0,100)
     If randy=<1 Or (randy=<10 And hardReleased(v)=gamDate) Then negVariable=v : negTopic=12
    EndIf
   EndIf
  Next
 EndIf
 ;mission assignments
 If gamMission=0 And charExperience(gamChar,fed)>4 And negTopic<>11
  randy=Rnd(0,50)
  If gamDate>projDate(gamProject)+4 And projReleased(gamProject)=0
   If randy=1 And projGameplay(gamProject)<hardGameplay(projFormat(gamProject))/2 Then negTopic=101 ;improve design
   If randy=2 And projReplay(gamProject)<hardReplay(projFormat(gamProject))/2 Then negTopic=102 ;improve programming
   If randy=3 And proj2D(gamProject)<hard2D(projFormat(gamProject))/2 Then negTopic=103 ;improve 2D
   If randy=4 And proj3D(gamProject)<hard3D(projFormat(gamProject))/2 Then negTopic=104 ;improve 3D
   If randy=5 And projSound(gamProject)<hardSound(projFormat(gamProject))/2 Then negTopic=105 ;improve sound
   If randy=6 And projMusic(gamProject)<hardMusic(projFormat(gamProject))/2 Then negTopic=106 ;improve music
   If (randy=>7 And randy=<11) Or (randy=>7 And randy=<15 And fedPolicy(fed)=8) Then negTopic=114 ;release project
  EndIf 
  If fed<no_feds
   If randy=16 And TeamSize(gamChar)<optTeanSize-1 Then negTopic=107 ;increase team size
   If randy=17 And TeamSize(gamChar)<optTeamSize Then negTopic=112 ;acquire artist
   If randy=18
    For count=1 To optTeamSize
     char=charRoster(gamChar,count)
     If char>0 And char<>gamChar And charContract(char)=>1 And charContract(char)=<4 Then negTopic=113 ;remove artist
    Next
   EndIf
  EndIf
  If randy=19 And gamBank<1000000 Then negTopic=109 ;improve bank balance
  If randy=>20 And randy=<25 And fed=no_feds And charExperience(gamChar,fed)>12 Then negTopic=111 ;get a deal!
  If gamBank<0 Then negTopic=110 ;get out of debt!
 EndIf
 ;mission completions
 If gamMission=1 And projGameplay(gamProject)=>gamTarget Then negTopic=120 ;improved gameplay
 If gamMission=2 And projReplay(gamProject)=>gamTarget Then negTopic=120 ;improved replay
 If gamMission=3 And proj2D(gamProject)=>gamTarget Then negTopic=120 ;improved 2D
 If gamMission=4 And proj3D(gamProject)=>gamTarget Then negTopic=120 ;improved 3D
 If gamMission=5 And projSound(gamProject)=>gamTarget Then negTopic=120 ;improved sound
 If gamMission=6 And projMusic(gamProject)=>gamTarget Then negTopic=120 ;improved music 
 If gamMission=7 And TeamSize(gamChar)=>gamTarget Then negTopic=120 ;increased team size 
 If gamMission=9 And gamBank=>gamTarget Then negTopic=121 ;improved bank balance
 If gamMission=10 And gamBank=>0 Then negTopic=121 ;got out of debt
 If gamMission=11 And charFed(gamChar)<no_feds Then gamMission=0 ;negTopic=120 ;got a deal
 If gamMission=12
  If charTeam(gamTarget)=gamChar Then negTopic=120 ;acquired artist
 EndIf
 If gamMission=13
  If charTeam(gamTarget)<>gamChar Then negTopic=120 ;removed artist
 EndIf
 If gamMission=14
  If projReleased(gamTarget)>0 Then gamMission=0;negTopic=120 ;released project
 EndIf
 ;mission failure
 If gamMission>0 And gamMission<>14 And gamDate=>gamDeadline And negTopic<120
  If fed=no_feds Then negTopic=131 Else negTopic=130
 EndIf
 ;game over!
 If GetYear(gamDate)>2013 And CountMachines(2)=0 Then negTopic=-1 
 ;execute
 If negTopic<>0 And negChar>0 And screen<>25 And screen<>29 Then negSetting=1 : screen=53 : screenAgenda=0 : Meeting() 
End Function

;----------------------------------------------------------------
;///////////////////// RISK TEAM MEETINGS ///////////////////////
;----------------------------------------------------------------
Function RiskTeamMeetings()
 fed=charFed(gamChar) 
 For cyc=1 To optTeamSize
  negTopic=0 : negSetting=0
  negChar=charRoster(gamChar,cyc)
  If negChar<>gamChar And charVacant(negChar)=0
   ;random suggestions
   If (gamMission=>1 And gamMission=<6) Or gamMission=14 Then mission=1 Else mission=0
   randy=Rnd(0,50)
   If randy=0 And TeamSize(gamChar)<optTeamSize And fedSize(fed)<optRosterLim Then negTopic=25 ;hire friend
   If randy=1 And gamAgreement(1)=0 And mission=0 And CountReleases()>1 And CountMachines(1)>1 Then negTopic=4 ;port old project
   If randy=2 And gamAgreement(1)=0 And mission=0 Then negTopic=5 ;game idea 
   If randy=3 And projReleased(gamProject)=0 Then negTopic=6 ;aspect of game
   If randy=4 And projReleased(gamProject)=0 And gamDate>projDate(gamProject)+4
    If projIdentified(gamProject)>0 And projIdentity(gamProject)>0 Then negTopic=7 ;release game
   EndIf
   ;dissent 
   chance=charRep(negChar)*4
   randy=Rnd(0,chance)
   If randy=0 And charVacant(negChar)=0 And charExperience(negChar,0)>4 Then negTopic=24 ;ask for a break
   If randy=1 And charContract(negChar)>1 And charExperience(negChar,0)>4 Then negTopic=28 ;ask for a release
   If randy=2 And gamBank>0 Then negTopic=29 ;held up for money
   If randy=3 And TeamSize(gamChar)>2 Then negTopic=26 ;remove enemy
   If randy=4 And TeamSize(gamChar)>2 Then negTopic=27 ;send enemy home
   If randy=5
    If CountRelationships(gamChar,1)>5 Then negTopic=31 ;ask to give up friend 
   EndIf
   ;repairs
   randy=Rnd(0,50)
   If (randy=5 And gamDesk(cyc)=1) Or (randy=<10 And gamDesk(cyc)=-1)
    gamDesk(cyc)=-1 : negSetting=6 : negTopic=8
   EndIf
   ;bounced cheque
   randy=Rnd(0,5) 
   If randy=<1 And gamBank<0 And charAgreement(negChar,1)=0 Then negTopic=9
  EndIf
  ;contract renewal
  If charContract(negChar)=0 And negChar<>gamChar Then negTopic=-1
  ;execute
  If negChar>0 And screen<>25 And screen<>29
   If negTopic>0 Then screen=53 : screenAgenda=0 : Meeting() 
   If negTopic=-1 Then negTopic=1 : negSetting=1 : screen=54 : screenAgenda=0 : RecruitMember() 
  EndIf
 Next
End Function

;---------------------------------------------------------------
;///////////////// LOAD MEETING SETTING ////////////////////////
;---------------------------------------------------------------
Function PrepareMeeting()
 ;CALCULATE CAST
 ResetTextures()
 For count=1 To optPlayLim
  pChar(count)=0 : pDesk(count)=0
 Next
 no_plays=2
 pChar(1)=gamChar : pChar(2)=negChar
 negStar=1 : negVisitor=2
 camFoc=negVisitor
 ;recruitment reversal
 If screen=54 And negSetting=1
  If fed=charFed(gamChar)
   pChar(1)=negChar : pChar(2)=gamChar
   negStar=2 : negVisitor=1
  EndIf
  If negTopic=2 Then camFoc=negVisitor Else camFoc=negStar
 EndIf
 ;office entourage
 If negSetting=1 And optEntourage>0
  no_plays=6 : spot=2
  For count=1 To optTeamSize
   char=charRoster(gamChar,Rnd(1,optTeamSize))
   If screen=53 And negTopic=20 And spot=2 Then char=negVariable
   exist=0
   For v=1 To no_plays
    If pChar(v)=char Then exist=1
   Next
   If char>0 And exist=0 And charVacant(char)=0
    If spot=<3 Or (optEntourage=2 And spot=<5) Then spot=spot+1 : pChar(spot)=char
   EndIf
  Next
 EndIf
 ;label owner
 If screen=54 And negSetting=1 And charFed(negChar)<>charFed(gamChar)
  If fedBooker(charFed(negChar))>0 And fedBooker(charFed(negChar))<>negChar Then no_plays=7 : pChar(no_plays)=fedBooker(charFed(negChar))  
 EndIf
 ;LOCATION
 ;boardroom locations
 Loader("","")
 If negSetting=>1 And negSetting=<5 
  world=LoadAnimMesh("World/Office/Office.3ds")
  HideEntity FindChild(world,"Laptop01") : HideEntity FindChild(world,"Laptop02")
  DecorateOffice(charFed(pChar(2)))
  If negSetting=1 And camFoc=1 ;seated (aiming for guest)
   randy=Rnd(1,2)
   If randy=1 Then camX#=-80 : camY#=50 : camZ#=90
   If randy=2 Then camX#=100 : camY#=50 : camZ#=-80
  EndIf
  If negSetting=1 And camFoc=2 ;seated (aiming for host)
   randy=Rnd(1,2)
   If randy=1 Then camX#=-90 : camY#=50 : camZ#=-90
   If randy=2 Then camX#=90 : camY#=50 : camZ#=-90
  EndIf
  If negSetting=2 ;north east
   camX#=-80 : camY#=Rnd(25,50) : camZ#=-80
   meetX#=65 : meetY#=5 : meetZ#=80 : meetA#=135
  EndIf
  If negSetting=3 ;south east
   camX#=-90 : camY#=Rnd(10,50) : camZ#=90
   meetX#=60 : meetY#=5 : meetZ#=-70 : meetA#=45
  EndIf
  If negSetting=4 ;south west
   camX#=90 : camY#=Rnd(10,50) : camZ#=90
   meetX#=-55 : meetY#=5 : meetZ#=-60 : meetA#=315
  EndIf
  If negSetting=5 ;north west
   camX#=80 : camY#=Rnd(25,50) : camZ#=-80
   meetX#=-70 : meetY#=5 : meetZ#=80 : meetA#=225
  EndIf
  lightX#=0 : lightY#=65 : lightZ#=0
  wNorth#=110 : wSouth#=-110
  wWest#=-100 : wEast#=105
  wGround#=5 : wCeiling#=75
 EndIf
 ;studio locations
 If negSetting=>6
  LoadWorkstations()
  If screen=53 And charTeam(negChar)<>gamChar
   no_plays=no_plays+1
   pChar(no_plays)=negChar
   negVisitor=no_plays
  EndIf
  camFoc=negVisitor
 EndIf
 ;universal scenery
 PrepareScenery()
 ;camera & lights
 Loader("","") 
 LoadCamera()
 LoadLighting()
 ;CHARACTERS
 ;office positions
 If negSetting=1
  pX#(1)=0 : pY#(1)=5 : pZ#(1)=-4 : pA#(1)=0 : pAnim(1)=Rnd(2,9)
  If pAnim(1)<4 Then pAnim(1)=9
  pX#(2)=0 : pY#(2)=5 : pZ#(2)=67 : pA#(2)=180 : pAnim(2)=Rnd(1,8)
  If gamChar=pChar(1)
   pX#(3)=10 : pY#(3)=5 : pZ#(3)=-23 : pA#(3)=15 : pAnim(3)=Rnd(20,23)
   pX#(4)=-10 : pY#(4)=5 : pZ#(4)=-23 : pA#(4)=345 : pAnim(4)=Rnd(20,23) 
   pX#(5)=20 : pY#(5)=5 : pZ#(5)=-8 : pA#(5)=45 : pAnim(5)=Rnd(20,23)
   pX#(6)=-20 : pY#(6)=5 : pZ#(6)=-8 : pA#(6)=315 : pAnim(6)=Rnd(20,23)
  Else
   pX#(3)=-10 : pY#(3)=5 : pZ#(3)=90 : pA#(3)=195 : pAnim(3)=Rnd(20,23)
   pX#(4)=10 : pY#(4)=5 : pZ#(4)=90 : pA#(4)=165 : pAnim(4)=Rnd(20,23) 
   pX#(5)=-20 : pY#(5)=5 : pZ#(5)=75 : pA#(5)=225 : pAnim(5)=Rnd(20,23)
   pX#(6)=20 : pY#(6)=5 : pZ#(6)=75 : pA#(6)=135 : pAnim(6)=Rnd(20,23)
  EndIf
  If negChar=pChar(1)
   pX#(7)=15 : pY#(7)=5 : pZ#(7)=-23 : pA#(7)=15 : pAnim(7)=Rnd(20,23)
  Else
   pX#(7)=-15 : pY#(7)=5 : pZ#(7)=90 : pA#(7)=195 : pAnim(7)=Rnd(20,23)
  EndIf
 EndIf
 ;standing positions
 If (negSetting=>2 And negSetting=<5) Or negSetting=8
  PositionEntity dummy,meetX#,meetY#,meetZ#
  RotateEntity dummy,0,meetA#,0
  MoveEntity dummy,12,0,0
  pX#(negStar)=EntityX(dummy) : pY#(negStar)=meetY# : pZ#(negStar)=EntityZ(dummy) : pA#(negStar)=meetA#+60 : pAnim(negStar)=Rnd(20,23)
  PositionEntity dummy,meetX#,meetY#,meetZ#
  RotateEntity dummy,0,meetA#,0
  MoveEntity dummy,-12,0,0
  pX#(negVisitor)=EntityX(dummy) : pY#(negVisitor)=meetY# : pZ#(negVisitor)=EntityZ(dummy) : pA#(negVisitor)=meetA#-60 : pAnim(negVisitor)=Rnd(20,23)
 EndIf
 ;visit workstations
 If negSetting=6 Or negSetting=7
  If negSetting=6 Then cyc=negStar : v=negVisitor
  If negSetting=7 Then cyc=negVisitor : v=negStar
  PositionEntity dummy,deskX#(v),deskY#(v),deskZ#(v)
  RotateEntity dummy,0,deskA#(v),0
  MoveEntity dummy,-20,0,-5
  pX#(cyc)=EntityX(dummy) : pY#(cyc)=meetY# : pZ#(cyc)=EntityZ(dummy) : pA#(cyc)=deskA#(v)+Rnd(290,340) : pAnim(cyc)=Rnd(20,23)
 EndIf
 ;load actors
 For cyc=1 To no_plays
  If pChar(cyc)>0
   LoadActor(cyc,pChar(cyc))
  EndIf
 Next
 ;restore textures
 RestoreTextures()
 ;point camera at camfoc
 camPivX#=pX#(camFoc) : camPivY#=pY#(camFoc)+30 : camPivZ#=pZ#(camFoc)
 PositionEntity camPivot,camPivX#,camPivY#,camPivZ#
 pFoc(negStar)=negVisitor : pFoc(negVisitor)=negStar
End Function

;---------------------------------------------------------------
;//////////////// LOAD MEETING CHARACTER ///////////////////////
;---------------------------------------------------------------
Function LoadActor(cyc,char)
 ;generate model
 Loader("","")
 p(cyc)=LoadAnimMesh("World/Workstations/"+textBuild$(charBuild(char))+".3ds")
 ApplyCostume(cyc)
 pEyes(cyc)=charEyes(pChar(cyc)) : pOldEyes(cyc)=-1
 pFoc(cyc)=0
 ;load sequences
 ;Loader("","")
 pSeq(cyc,1000)=LoadAnimSeq(p(cyc),"Characters/Animation/Female.3ds") 
 pSeq(cyc,1002)=LoadAnimSeq(p(cyc),"Characters/Animation/Dancing.3ds")
 pSeq(cyc,1003)=LoadAnimSeq(p(cyc),"Characters/Animation/Handhelds.3ds")
 pSeq(cyc,1010)=LoadAnimSeq(p(cyc),"Characters/Animation/Gestures.3ds") 
 ;sitting animations
 pSeq(cyc,1)=ExtractAnimSeq(p(cyc),1480,1520,pSeq(cyc,1010)) ;sitting at desk (hands spread)
 pSeq(cyc,2)=ExtractAnimSeq(p(cyc),2280,2320,pSeq(cyc,1010)) ;sitting at desk (hands overlapped)
 pSeq(cyc,3)=ExtractAnimSeq(p(cyc),2330,2370,pSeq(cyc,1010)) ;sitting at desk (hands clasped)
 pSeq(cyc,4)=ExtractAnimSeq(p(cyc),2180,2220,pSeq(cyc,1010)) ;sitting rubbing chin
 pSeq(cyc,5)=ExtractAnimSeq(p(cyc),2230,2270,pSeq(cyc,1010)) ;sitting arms folded
 pSeq(cyc,6)=ExtractAnimSeq(p(cyc),2480,2520,pSeq(cyc,1010)) ;sitting relaxed
 pSeq(cyc,7)=ExtractAnimSeq(p(cyc),2430,2470,pSeq(cyc,1010)) ;sitting hands behind head
 pSeq(cyc,8)=ExtractAnimSeq(p(cyc),2380,2420,pSeq(cyc,1010)) ;sitting arms on sides
 pSeq(cyc,9)=ExtractAnimSeq(p(cyc),1530,1570,pSeq(cyc,1010)) ;sitting cross-legged
 pSeq(cyc,10)=ExtractAnimSeq(p(cyc),2770,2850,pSeq(cyc,1010)) ;typing
 pSeq(cyc,11)=ExtractAnimSeq(p(cyc),2860,2920,pSeq(cyc,1010)) ;tired slump

 pSeq(cyc,15)=ExtractAnimSeq(p(cyc),910,970,pSeq(cyc,1003)) ;sitting with box
 pSeq(cyc,16)=ExtractAnimSeq(p(cyc),840,900,pSeq(cyc,1003)) ;sitting with vertical handheld
 pSeq(cyc,17)=ExtractAnimSeq(p(cyc),700,760,pSeq(cyc,1003)) ;sitting with horizontal handheld
 pSeq(cyc,18)=ExtractAnimSeq(p(cyc),770,830,pSeq(cyc,1003)) ;sitting with book handheld
 ;standing animations
 pSeq(cyc,20)=ExtractAnimSeq(p(cyc),490,530,pSeq(cyc,1002)) ;standing straight
 pSeq(cyc,21)=ExtractAnimSeq(p(cyc),2110,2170,pSeq(cyc,1010)) ;standing rubbing chin
 pSeq(cyc,22)=ExtractAnimSeq(p(cyc),160,220,pSeq(cyc,1002)) ;standing hands on hips
 pSeq(cyc,23)=ExtractAnimSeq(p(cyc),2020,2080,pSeq(cyc,1010)) ;standing folded arms

 pSeq(cyc,25)=ExtractAnimSeq(p(cyc),980,1040,pSeq(cyc,1003)) ;standing with box
 pSeq(cyc,26)=ExtractAnimSeq(p(cyc),630,690,pSeq(cyc,1003)) ;standing with vertical handheld
 pSeq(cyc,27)=ExtractAnimSeq(p(cyc),490,550,pSeq(cyc,1003)) ;standing with horizontal handheld
 pSeq(cyc,28)=ExtractAnimSeq(p(cyc),560,620,pSeq(cyc,1003)) ;standing with book handheld
 pSeq(cyc,29)=ExtractAnimSeq(p(cyc),70,110,pSeq(cyc,1010)) ;injured stance
 pSeq(cyc,30)=ExtractAnimSeq(p(cyc),135,175,pSeq(cyc,1000)) ;feminine
 ;determine animation
 If charWorked(char)>0 And (screenAgenda=2 Or (screenAgenda=4 And char<>gamWorker))
  If pAnim(cyc)=<10 Then pAnim(cyc)=11
 EndIf
 ;If charWorked(char)>0 And pAnim(cyc)=>20 Then pAnim(cyc)=29
 If screen=50 And pAnim(cyc)=10 And charWorked(char)>0 Then pAnim(cyc)=Rnd(1,9)
 If GetGender(char)=1 And pAnim(cyc)=>20 And pAnim(cyc)=<23 Then pAnim(cyc)=30
 ;game box
 boxer=0
 If screen=50 And screenAgenda=0 Then boxer=1
 If screen=53 And negTopic=>2 And negTopic=<4 Then boxer=1
 If screen=53 And projIdentified(gamProject)>0
  If (negTopic=>6 And negTopic=<7) Or (negTopic=>101 And negTopic=<106) Or negTopic=114 Then boxer=1
 EndIf
 If boxer>0
  If pAnim(cyc)<20 Then pAnim(cyc)=15
  If pAnim(cyc)=>20 Then pAnim(cyc)=25
 EndIf
 ;handhelds
 If screenAgenda=8 And pChar(cyc)=gamChar Then hardy=gamNewHardware Else hardy=gamHardware
 If negSetting=>6 And charTeam(char)=gamChar And hardHandheld(hardy)>0 And (projIdentity(gamProject)>0 Or screenAgenda=8)
  If charWorked(char)>0 Or screen<>50 Or screenAgenda=0 Or (screenAgenda=6 And char=gamChar) Or (screenAgenda=8 And char=gamChar)
   If pAnim(cyc)<20 Then pAnim(cyc)=15+hardHandheld(hardy)
   If pAnim(cyc)=>20 Then pAnim(cyc)=25+hardHandheld(hardy)
   If desk(cyc)>0  
    HideEntity FindChild(desk(cyc),hardModel$(hardy))
    HideEntity FindChild(desk(cyc),hardTV$(hardy)+"S")
    If FindChild(desk(cyc),hardCom$(hardy)+"S")>0
     HideEntity FindChild(desk(cyc),hardCom$(hardy)+"S")
    EndIf
   EndIf
  EndIf
 EndIf
 ;start animation
 animSpeed#=Rnd#(0.1,0.3) 
 If pAnim(cyc)=11 Then animSpeed#=Rnd#(0.4,0.7)
 If (pAnim(cyc)=>15 And pAnim(cyc)=<18) Or (pAnim(cyc)=>25 And pAnim(cyc)=<28) Then animSpeed#=Rnd#(0.4,0.7)
 If pAnim(cyc)=10 Then animSpeed#=Rnd#(0.8,1.3)
 Animate p(cyc),1,animSpeed#,pSeq(cyc,pAnim(cyc)),0
 pOldAnim(cyc)=pAnim(cyc)
 ApplyHandhelds(cyc)
 ;orientation
 PositionEntity p(cyc),pX#(cyc),pY#(cyc),pZ#(cyc)
 RotateEntity p(cyc),0,pA#(cyc),0
 scaler#=0.055+(GetPercent#(charHeight(char),24)/10000)
 If pAnim(cyc)<20 Then scaler#=0.06
 ScaleEntity p(cyc),scaler#,scaler#,scaler# 
 ;shadows
 LoadShadows(cyc)
End Function

;----------------------------------------------------------------------------
;//////////////////////// 53. MEETING PROCESS ///////////////////////////////
;----------------------------------------------------------------------------
Function Meeting()
;target game to make
If negTopic=2 Or negTopic=3 Or negTopic=5
 its=0
 Repeat
  negVariable=Rnd(1,no_games) : satisfied=1 : its=its+1
  If softGameplay(negVariable)=>hardGameplay(gamHardware) Then satisfied=0
  If softReplay(negVariable)=>hardReplay(gamHardware) Then satisfied=0
  If soft2D(negVariable)=>hard2D(gamHardware) Then satisfied=0
  If soft3D(negVariable)=>hard3D(gamHardware) Then satisfied=0
  If softSound(negVariable)=>hardSound(gamHardware) Then satisfied=0
  If softMusic(negVariable)=>hardMusic(gamHardware) Then satisfied=0
  For count=1 To no_projects
   If projIdentity(count)=negVariable Then satisfied=0
  Next
 Until satisfied=1 Or its>10000
EndIf
;target project to port
If negTopic=4
 its=0
 Repeat
  negVariable=Rnd(1,no_projects) : negFormat=Rnd(1,no_machines)
  satisfied=1 : its=its+1
  If projReleased(negVariable)=0 Then satisfied=0
  If its<100 And hardUsers(negFormat)<10000 Then satisfied=0
  If hardReleased(negFormat)=0 Or hardOwned(negFormat)=0 Then satisfied=0
  For v=1 To no_projects
   If projIdentity(v)=projIdentity(negVariable) And projFormat(v)=negFormat Then satisfied=0
  Next
 Until satisfied=1 Or its>1000
EndIf 
;expert trainer
If negTopic=10
 its=0
 Repeat
  negVariable=Rnd(1,6)
  negChar=Rnd(1,no_chars) : satisfied=1 : its=its+1
  If charTeam(negChar)=gamChar Or charRelationship(negChar,gamChar)<0 Or charFed(negChar)=charFed(gamChar) Or charFed(negChar)=0 Then satisfied=0
  If negVariable=1 And charDesign(negChar)<80 Then satisfied=0
  If negVariable=2 And charProgram(negChar)<80 Then satisfied=0
  If negVariable=3 And char2D(negChar)<80 Then satisfied=0
  If negVariable=4 And char3D(negChar)<80 Then satisfied=0
  If negVariable=5 And charSound(negChar)<80 Then satisfied=0
  If negVariable=6 And charMusic(negChar)<80 Then satisfied=0
 Until satisfied=1 Or its>1000
EndIf 
;collaborator
If negTopic=14 Or negTopic=15
 Repeat
  negVariable=Rnd(1,6) : negChar=Rnd(1,no_chars) : satisfied=1
  If charTeam(negChar)=gamChar Or charRelationship(negChar,gamChar)<0 Or charFed(negChar)=charFed(gamChar) Or charFed(negChar)=0 Then satisfied=0
 Until satisfied=1
EndIf
;target employee to sell
If negTopic=20
 Repeat
  negChar=fedBooker(Rnd(1,8)) 
 Until charFed(negChar)<>charFed(gamChar) And fedSize(charFed(negChar))<optRosterLim
 its=0
 Repeat
  negVariable=charRoster(gamChar,Rnd(1,optTeamSize)) : its=its+1
 Until (negVariable>0 And negVariable<>gamChar And negVariable<>negChar And charContract(negVariable)>0) Or its>1000
EndIf
;loading message
Loader("Please Wait","Meeting "+charName$(negChar))
;load setting
If negSetting=0
 negSetting=Rnd(7,9)
 If charTeam(negChar)=gamChar Then negSetting=Rnd(6,9)
 If negSetting>8 Then negSetting=8
 If negChar=fedBooker(charFed(negChar)) Then negSetting=1
EndIf
camFoc=negVisitor 
PrepareMeeting()
;target aspect to work on
If negTopic=6 Then negVariable=Rnd(1,6)
;target aspect to sell
If negTopic=16
 its=0
 Repeat
  negVariable=Rnd(1,6) : satisfied=1 : its=its+1
  If negVariable=1 And projGameplay(gamProject)<hardGameplay(projFormat(gamProject))/3 Then satisfied=0
  If negVariable=2 And projReplay(gamProject)<hardReplay(projFormat(gamProject))/3 Then satisfied=0
  If negVariable=3 And proj2D(gamProject)<hard2D(projFormat(gamProject))/3 Then satisfied=0
  If negVariable=4 And proj3D(gamProject)<hard3D(projFormat(gamProject))/3 Then satisfied=0
  If negVariable=5 And projSound(gamProject)<hardSound(projFormat(gamProject))/3 Then satisfied=0
  If negVariable=6 And projMusic(gamProject)<hardMusic(projFormat(gamProject))/3 Then satisfied=0
 Until satisfied=1 Or its>1000
EndIf
;target project to sell
If negTopic=17 Or negTopic=18 Or negTopic=19
 Repeat
  negVariable=Rnd(1,no_projects)
 Until projReleased(negVariable)>0
EndIf
;target friend to hire
If negTopic=25
 its=0
 Repeat
  negVariable=Rnd(1,no_chars) : its=its+1
 Until charFed(negVariable)>0 And charTeam(negVariable)<>gamChar And negVariable<>fedBooker(charFed(negVariable)) And (charRelationship(negChar,negVariable)>0 Or its>1000)
 ChangeRelationship(negChar,negVariable,1)
EndIf
;target enemy to remove
If negTopic=26 Or negTopic=27
 its=0
 Repeat
  negVariable=charRoster(gamChar,Rnd(1,optTeamSize)) : its=its+1
 Until negVariable>0 And negVariable<>gamChar And negVariable<>negChar And (charVacant(negVariable)=0 Or its>100) And (charRelationship(negChar,negVariable)<0 Or its>100)
 ChangeRelationship(negChar,negVariable,-1)
EndIf
;target friend to give up
If negTopic=31
 its=0
 Repeat
  negVariable=Rnd(1,no_chars) : its=its+1
 Until charFed(negVariable)>0 And charTeam(negVariable)<>gamChar And (charRelationship(gamChar,negVariable)>0 Or its>1000)
 ChangeRelationship(gamChar,negVariable,1)
EndIf
;target character for mission
If negTopic=112
 Repeat
  gamTarget=Rnd(1,no_chars)
 Until charFed(gamTarget)>0 And charTeam(gamTarget)<>gamChar And gamTarget<>fedBooker(no_feds)
EndIf
If negTopic=113
 its=0
 Repeat
  gamTarget=charRoster(gamChar,Rnd(1,optTeamSize)) : its=its+1
 Until gamTarget>0 And gamTarget<>gamChar And (charContract(gamTarget)=<4 Or its>100)
EndIf
;reset situation
negStage=0 : negTim=0
negVerdict=0
;bribe amount
negPayOff=(charRep(gamChar)*charRep(gamChar))*5
If negTopic=2 Or negTopic=3 Or negTopic=13 Then negPayOff=(charRep(gamChar)*charRep(gamChar))*10
If negTopic=10 Then negPayOff=(charRep(negChar)*charRep(negChar))*1
If negTopic=9 Or negTopic=29 Then negPayOff=(charRep(negChar)*charRep(negChar))*5
If negTopic=14 Then negPayOff=(charRep(negChar)*charRep(negChar))*4
If negTopic=15 Then negPayOff=(charRep(negChar)*charRep(negChar))*10
If negTopic=16
 If negVariable=1 Then negPayOff=(projGameplay(gamProject)*projGameplay(gamProject))*5
 If negVariable=2 Then negPayOff=(projReplay(gamProject)*projReplay(gamProject))*5
 If negVariable=3 Then negPayOff=(proj2D(gamProject)*proj2D(gamProject))*5
 If negVariable=4 Then negPayOff=(proj3D(gamProject)*proj3D(gamProject))*5
 If negVariable=5 Then negPayOff=(projSound(gamProject)*projSound(gamProject))*5
 If negVariable=6 Then negPayOff=(projMusic(gamProject)*projMusic(gamProject))*5
EndIf
If negTopic=17 Or negTopic=18
 negPayOff=(AverageProjectStats(negVariable,1)*AverageProjectStats(negVariable,1))*20
EndIf
If negTopic=19
 negPayOff=(projMusic(negVariable)*projMusic(negVariable))*5
EndIf
If negTopic=20
 negPayOff=CalculateWorth(negVariable)*charContract(negVariable)
 negPayOff=PercentOf#(negPayOff,Rnd(100,150))
EndIf
negPayOff=PercentOf#(negPayOff,Rnd(75,125))
negPayOff=RoundOff(negPayOff,1000)
If negPayOff<1000 Then negPayOff=1000
If negPayOff>1000000 Then negPayOff=1000000
If charFed(gamChar)=no_feds Then negPayOff=negPayOff/2
If negTopic=8 Then negPayOff=gamRepairCost
If negTopic=11 Then negPayOff=hardCost(negVariable)/2
;missions
If negTopic=>101 And negTopic<120
 randy=Rnd(4,8)
 If optTime=2 Then gamDeadline=gamDate+(randy*4) Else gamDeadline=gamDate+randy
EndIf
If negTopic=120 Then negPayOff=10000
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=1 : oldfoc=foc : charged=0
go=0 : gotim=-20 : keytim=20
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PORTAL 
    gotim=gotim+1
    If gotim>25 Then negTim=negTim+1 
    ;speed-ups
    If gotim>0 And negStage<>1 And negStage<>3 And keytim=0
	 If KeyDown(1) Or MouseDown(1) Then negTim=negTim+50 : keytim=5 ;: PlaySound sMenuBrowse
	EndIf
	;manage music
	ManageMusic(0)
	
	;CHOICE CONFIRMATION
	If keytim=0 And negStage=1
	 ;proceed 
     If KeyDown(1) Or (MouseDown(1) And foc>0)
	  PlaySound sMenuGo : keytim=20
	  negStage=2 : negTim=0
	  If foc=1 Then negVerdict=1
	  If foc=2 Or KeyDown(1) Then negVerdict=-1
	 EndIf
	EndIf
	
	;SPEAKING
	For cyc=1 To no_plays
	 If pChar(cyc)>0
	  ;change animation
      RandomizeAnimation(cyc)
	  ;facial expressions
	  FacialExpressions(cyc)
	  ;shadows
	  For limb=1 To 50
       If pShadow(cyc,limb)>0
        RotateEntity pShadow(cyc,limb),90,EntityYaw(pLimb(cyc,limb),1),0
        PositionEntity pShadow(cyc,limb),EntityX(pLimb(cyc,limb),1),pY#(cyc)+0.4,EntityZ(pLimb(cyc,limb),1)
       EndIf
      Next
     EndIf 
	Next
	
	;CAMERA
	Camera()  
	
 UpdateWorld
 Next  
 ;animation override
 For cyc=1 To no_plays
  viable=1
  If negSetting=1
   If cyc=1 And pFoc(cyc)=2 Then viable=0
   If cyc=2 And pFoc(cyc)=1 Then viable=0
  EndIf
  If viable=1 And pChar(cyc)>0
   If pFoc(cyc)>0 And pChar(pFoc(cyc))>0
    PointHead(cyc,FindChild(p(pFoc(cyc)),"Head"))
   ;Else
    ;If desk(camFoc)>0
     ;PointHead(cyc,FindChild(desk(camFoc),hardTV$(deskHardware(camFoc))+"S"))
    ;EndIf
   EndIf
  EndIf
 Next

 RenderWorld 1

 ;DISPLAY
 ;DrawImage gLogo(2),rX#(400),rY#(60)
 ;reset expressions
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------------------ TOPICS ----------------------------
 ;-1. INDUSTRY OVER
 If negTopic=-1
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Well, "+charName$(gamChar)+", now that it's "+GetYear(gamDate)+" it seems"
   lineB$="human beings have moved on from playing games!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="We'll all have to pack up and find a new outlet for"
   lineB$="our creativity! Quite what that will be nobody knows..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,3)
   lineA$="I hope you've enjoyed this journey as much as I have."
   lineB$="Maybe I'll bump into you at the Game Museum some time..."
  EndIf
  If negStage=0 And negTim>975 Then camFoc=negStar : pEyes(camFoc)=3
  If negStage=0 And negTim>1125 Then go=1 
 EndIf
 ;0. INTRO
 If negTopic=0
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Welcome to the games industry, "+charName$(gamChar)+"!"
   lineB$="I'm sure you can't wait to get started."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I'm here to help independent developers find"
   lineB$="their feet before they sign with a major company."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,2)
   lineA$="Although we have limited distribution channels,"
   lineB$="we can at least help you release your projects."
  EndIf
  If negStage=0 And negTim>1000 And negTim<1300
   Speak(negVisitor,negStar,3)
   lineA$="We split the profits 50-50, so there's plenty"
   lineB$="of money to be made if you put in the effort!"
  EndIf
  If negStage=0 And negTim>1325 And negTim<1625
   Speak(negVisitor,negStar,2)
   lineA$="Be sure to manage your finances carefully though,"
   lineB$="because there are a lot of expenses involved."
  EndIf
  If negStage=0 And negTim>1650 And negTim<1950
   Speak(negVisitor,negStar,2)
   lineA$="The latest software development kits cost a fortune,"
   lineB$="and you have to pay rent on each workstation you use!"
  EndIf
  If negStage=0 And negTim>1975 And negTim<2275
   Speak(negVisitor,negStar,3)
   lineA$="If you feel you need help with a project, we have plenty"
   lineB$="of other developers whose expertise you can hire."
  EndIf
  If negStage=0 And negTim>2300 And negTim<2600
   Speak(negVisitor,negStar,3)
   lineA$="Eventually you'll want to branch out into mainstream"
   lineB$="games - but you have to make a name for yourself first."
  EndIf
  If negStage=0 And negTim>2625 And negTim<2925
   Speak(negVisitor,negStar,3)
   lineA$="For now, just concentrate on making the best games you"
   lineB$="can and we'll see what destiny has in store for you!"
  EndIf
  If negStage=0 And negTim>2925 Then camFoc=negStar : pEyes(camFoc)=3
  If negStage=0 And negTim>3075 Then go=1 
 EndIf
 ;1. RETIREMENT CONFIRMATION
 If negTopic=1
  optionA$="Yes, retire..." : optionB$="No, keep working!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", what's all this I hear about"
   lineB$="retirement?! Are you sure you want to call it a day?"
  EndIf
  If negStage=0 And negTim>325 Then camFoc=negStar
  If negStage=0 And negTim>350 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,1)
   lineA$="If that's what you want, I guess there's nothing left"
   lineB$="to say. Some people just haven't got what it takes..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,3)
   lineA$="Good, I hate to see people throwing away their careers!"
   lineB$="Remember, you've only ever failed when you give up..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;2. PORT ARCADE HIT
 If negTopic=2
  optionA$="Yes, port game!" : optionB$="No, too busy."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", have you ever heard of a game called"
   lineB$="'"+softTitle$(negVariable)+"'? It's doing good business in the arcades!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="How would you feel about creating a "+hardTitle$(gamHardware)
   lineB$="version of the game if we bought the rights to it?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PortGame(negVariable) : gamAgreement(1)=99
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+PursueValue(charRep(gamChar),99,0)  
    charged=1
   EndIf
   lineA$="Great, '"+softTitle$(negVariable)+"' is a license to print money!"
   lineB$="Focus all your efforts on bringing it to market..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="Fine, I'll just give the opportunity to someone else!"
   lineB$="'"+softTitle$(negVariable)+"' is a license to print money..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;3. ASSIGNED PROJECT
 If negTopic=3
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, too busy."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", we've been offered $"+GetFigure$(negPayOff)
   lineB$="to make a game called '"+softTitle$(negVariable)+"' for a client!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Do you have time to work on the assignment?"
   lineB$="It's guaranteed to be profitable for you..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    PortGame(negVariable) : gamAgreement(1)=99
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+1  
    charged=1
   EndIf
   lineA$="Excellent! I'll pass the brief onto you right away."
   lineB$="Maybe '"+softTitle$(negVariable)+"' will turn out to be a great game..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="Fine, I'll just give the opportunity to someone else!"
   lineB$="I'm sure '"+softTitle$(negVariable)+"' will turn out to be a great game..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;4. PORT OLD PROJECT
 If negTopic=4
  optionA$="Yes, port game!" : optionB$="No, move on."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", have you ever considered"
   lineB$="porting '"+projTitle$(negVariable)+"' to the "+hardTitle$(negFormat)+"?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="When all that work has gone into creating a game,"
   lineB$="there's no point in limiting it to one system!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PortProject(negVariable,negFormat)
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+1  
    charged=1
   EndIf
   lineA$="Great, porting an existing game gives us a headstart."
   lineB$="The "+hardTitle$(negFormat)+" version has a chance of being even better!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="What's the point in wasting all of that hard work?"
   lineB$="You can't reinvent the wheel with every project!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;5. COLLEAGUE HAS IDEA FOR GAME
 If negTopic=5
  optionA$="Yes, make game!" : optionB$="No, forget it."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", I've had a great idea for a game called"
   lineB$="'"+softTitle$(negVariable)+"'! Do you think we could start work on it?"
  EndIf
  If negStage=0 And negTim>325 Then camFoc=negStar
  If negStage=0 And negTim>350 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PortGame(negVariable) : gamAgreement(1)=99
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1  
    charged=1
   EndIf
   lineA$="Fantastic! I'll talk you through my vision for"
   lineB$="'"+softTitle$(negVariable)+"' and we'll take it from there..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1  : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, I'll just keep the idea for myself! Everybody"
   lineB$="will be talking about '"+softTitle$(negVariable)+"' one day..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;6. IDEA FOR ASPECT OF GAME
 If negTopic=6
  optionA$="Yes, go ahead!" : optionB$="No, know your role."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", I've got some great ideas for the "+textAttribute$(negVariable)
   If projIdentified(gamProject)>0 Then namer$="'"+projTitle$(gamProject)+"'" Else namer$="the latest project"
   lineB$="of "+namer$+"! Why don't you let me take control of that?"
  EndIf
  If negStage=0 And negTim>325 Then camFoc=negStar
  If negStage=0 And negTim>350 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    If negVariable=1 Then projGameplay(gamProject)=PercentOf#(hardGameplay(projFormat(gamProject)),charDesign(negChar))
    If negVariable=2 Then projReplay(gamProject)=PercentOf#(hardReplay(projFormat(gamProject)),charProgram(negChar))
    If negVariable=3 Then proj2D(gamProject)=PercentOf#(hard2D(projFormat(gamProject)),char2D(negChar))
    If negVariable=4 Then proj3D(gamProject)=PercentOf#(hard3D(projFormat(gamProject)),char3D(negChar))
    If negVariable=5 Then projSound(gamProject)=PercentOf#(hardSound(projFormat(gamProject)),charSound(negChar))
    If negVariable=6 Then projMusic(gamProject)=PercentOf#(hardMusic(projFormat(gamProject)),charMusic(negChar))
    projCredit(gamProject,negVariable)=negChar
	randy=Rnd(0,2)
    If randy=0 Then charDedicated(negChar)=negVariable
    If randy=<1 Then projLocked(gamProject,negVariable)=negChar
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1  
    charged=1
   EndIf
   lineA$="Great, thanks for placing so much faith in me!"
   lineB$="I'm sure you'll like what I do with the "+textAttribute$(negVariable)+"..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1  : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, I'll just keep my ideas to myself! We'll never"
   lineB$="know how good the "+textAttribute$(negVariable)+" could have been..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;7. SUGGEST RELEASE
 If negTopic=7
  optionA$="Yes, release game!" : optionB$="No, not yet."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", don't you think it's time we released"
   If projIdentified(gamProject)>0 Then namer$="'"+projTitle$(gamProject)+"'" Else namer$="the latest project"
   lineB$=namer$+"? It will lose its impact if we wait too long!"
  EndIf
  If negStage=0 And negTim>325 Then camFoc=negStar
  If negStage=0 And negTim>350 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1  
    charged=1
   EndIf
   lineA$="I'm glad you agree! The sooner it's out there,"
   lineB$="the sooner we can start earning money from it..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1  : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="We can't hide ourselves away and work on the game"
   lineB$="forever! Thousands of people are waiting to play it..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;8. PAY FOR REPAIRS
 If negTopic=8
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"." : optionB$="No, don't worry!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", my "+hardTitle$(gamHardware)+" is broken!"
   lineB$="It's not showing any signs of life."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="Are you going to pay $"+GetFigure$(negPayOff)+" to repair it?"
   lineB$="I won't be able to get any work done until you do!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
	For cyc=1 To optTeamSize
     If charRoster(gamChar,cyc)=negChar Then gamDesk(cyc)=-2
    Next
	charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+1  
    charged=1
   EndIf
   lineA$="Alright, thanks for addressing the problem!"
   lineB$="I should be able to get back to work soon..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    For cyc=1 To optTeamSize
     If charRoster(gamChar,cyc)=negChar Then gamDesk(cyc)=-1
    Next
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="And what exactly am I supposed to do in the"
   lineB$="meantime - start making games out of cardboard?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;9. CHECK BOUNCED
 If negTopic=9
  optionA$="Yes, promise $"+GetFigure$(negPayOff)+"." : optionB$="No, be patient!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", my last pay cheque bounced!"
   lineB$="You're in breach of our agreed contract..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="I may be willing to tolerate this situation if you"
   lineB$="promise me $"+GetFigure$(negPayOff)+"? Otherwise I'll walk out right now!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,1)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    charAgreement(negChar,1)=charContract(negChar)
    If charAgreement(negChar,1)<4 Then charAgreement(negChar,1)=4
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Alright, now concentrate on getting your finances"
   lineB$="in order so that you can pay the money you owe me!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    LeaveTeam(negChar)
    charContract(negChar)=0 : charNegotiated(negChar)=1
    ChangeRelationship(gamChar,negChar,-1) 
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Well, if you have no commitment to me then I no"
   lineB$="longer have any commitment to your broke ass!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;10. EXPERT TRAINING
 If negTopic=10
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+" x "+TeamSize(gamChar)+"!" : optionB$="No thanks."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+". As I'm sure"
   lineB$="you're aware, I'm one of the best "+textRole$(negVariable)+"s in the world!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I like to share my expertise and would be willing to"
   lineB$="train your team in "+textAttribute$(negVariable)+" for just $"+GetFigure$(negPayOff)+" per person?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash
    For cyc=1 To optTeamSize
     char=charRoster(gamChar,cyc)
     If char>0 And charVacant(char)=0
      gamBank=gamBank-negPayOff
      If negVariable=1 And charDesign(char)<charDesign(negChar) Then charDesign(char)=charDesign(char)+PursueValue(charDesign(char),charDesign(negChar),200)
      If negVariable=2 And charProgram(char)<charProgram(negChar) Then charProgram(char)=charProgram(char)+PursueValue(charProgram(char),charProgram(negChar),200)
      If negVariable=3 And char2D(char)<char2D(negChar) Then char2D(char)=char2D(char)+PursueValue(char2D(char),char2D(negChar),200)
      If negVariable=4 And char3D(char)<char3D(negChar) Then char3D(char)=char3D(char)+PursueValue(char3D(char),char3D(negChar),200)
      If negVariable=5 And charSound(char)<charSound(negChar) Then charSound(char)=charSound(char)+PursueValue(charSound(char),charSound(negChar),200)
      If negVariable=6 And charMusic(char)<charMusic(negChar) Then charMusic(char)=charMusic(char)+PursueValue(charMusic(char),charMusic(negChar),200)
      charRep(char)=charRep(char)+1
      charHappiness(char)=charHappiness(char)+1
     EndIf
    Next
    charged=1
   EndIf
   lineA$="My pleasure! Once I've finished instructing your team,"
   lineB$="they'll be creating better "+textAttribute$(negVariable)+" than ever before..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    For cyc=1 To optTeamSize
     char=charRoster(gamChar,cyc)
     If char>0 Then charRep(char)=charRep(char)-1 : charHappiness(char)=charHappiness(char)-1
    Next 
    charged=1
   EndIf
   lineA$="I understand. A lot of small-time operations like yours"
   lineB$="lack the ambition to do what it takes to succeed..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;11. SDK OFFER (internal)
 If negTopic=11
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hello, "+charName$(gamChar)+", I'm sure you're aware that"
   lineB$=hardBrand$(negVariable)+" have released the '"+hardTitle$(negVariable)+"'?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Well since you're a loyal "+fedName$(fed)+" employee, I can"
   lineB$="offer you one for the discounted price of $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    charRep(gamChar)=charRep(gamChar)+1 
    hardOwned(negVariable)=1 : charged=1
   EndIf
   lineA$="A wise investment! I look forward to seeing what kind"
   lineB$="of games you can produce for the '"+hardTitle$(negVariable)+"'..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0 Then charRep(gamChar)=charRep(gamChar)-1 : charged=1
   lineA$="Very well, then you must pay full price like everybody else!"
   lineB$="Everyone will be making games for the '"+hardTitle$(negVariable)+"' soon..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;12. SDK OFFER (external)
 If negTopic=12
  optionA$="Yes, accept gift!" : optionB$="No thanks."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hello, "+charName$(gamChar)+", I'm sure you're aware that"
   lineB$=hardBrand$(negVariable)+" have released the '"+hardTitle$(negVariable)+"'?"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Well they're willing to give you a FREE unit worth"
   lineB$="$"+GetFigure$(hardCost(negVariable))+" if you agree to make games for it exclusively?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    fedPolicy(charFed(gamChar))=1 : fedBrand$(charFed(gamChar))=hardBrand$(negVariable)
    hardOwned(negVariable)=1 : gamHardware=negVariable
	If projReleased(gamProject)=0 Then projFormat(gamProject)=negVariable
    charRep(gamChar)=charRep(gamChar)-1 : charged=1
   EndIf
   lineA$="A wise decision! I look forward to seeing what kind"
   lineB$="of games you can produce for the '"+hardTitle$(negVariable)+"'..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0 Then charRep(gamChar)=charRep(gamChar)+1 : charged=1
   lineA$="Very well, then you can pay full price like everybody else!"
   lineB$="Everyone will be making games for the '"+hardTitle$(negVariable)+"' soon..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;13. PAY TO PROMOTE GAME
 If negTopic=13
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No, forget it."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Listen, "+charName$(gamChar)+", success in this business is"
   lineB$="as much about promoting games as making them!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="If you could spare $"+GetFigure$(negPayOff)+" to promote '"+projTitle$(gamRelease)+"'"
   lineB$="I'm sure we'd see a significant increase in sales?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    chartPromotion#(1)=chartPromotion#(1)+25
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="A wise investment! Once we grease the wheels,"
   lineB$="'"+projTitle$(gamRelease)+"' has every chance of succeeding..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
	chartPromotion#(1)=chartPromotion#(1)-25
    charRep(gamChar)=charRep(gamChar)+1
    charged=1
   EndIf
   lineA$="Well if you're not prepared to invest in the project"
   lineB$="then perhaps we should withdraw our support too!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;14. BUY ASPECT OF WORK
 If negTopic=14
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+"."
   lineB$="I'd like to talk to you about your latest project..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I've already done some great work on the "+textAttribute$(negVariable)
   lineB$="for a game that I'm not likely to finish any time soon!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,3)
   lineA$="I'd be happy to sell my work on to you for just"
   lineB$="$"+GetFigure$(negPayOff)+" if you think you could do something with it?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=negStar
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    If negVariable=1 Then projGameplay(gamProject)=PercentOf#(hardGameplay(projFormat(gamProject)),charDesign(negChar))
    If negVariable=2 Then projReplay(gamProject)=PercentOf#(hardReplay(projFormat(gamProject)),charProgram(negChar))
    If negVariable=3 Then proj2D(gamProject)=PercentOf#(hard2D(projFormat(gamProject)),char2D(negChar))
    If negVariable=4 Then proj3D(gamProject)=PercentOf#(hard3D(projFormat(gamProject)),char3D(negChar))
    If negVariable=5 Then projSound(gamProject)=PercentOf#(hardSound(projFormat(gamProject)),charSound(negChar))
    If negVariable=6 Then projMusic(gamProject)=PercentOf#(hardMusic(projFormat(gamProject)),charMusic(negChar))
    projCredit(gamProject,negVariable)=negChar
    ChangeRelationship(gamChar,negChar,1) 
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Great, I'm glad my hard work won't go to waste!"
   lineB$="I look forward to seeing what you do with it..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, you're not worthy of my talent anyway!"
   lineB$="Don't come to me the next time you need help..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;15. BUY PROJECT
 If negTopic=15
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"!" : optionB$="No thanks."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+"."
   lineB$="I've got a proposition I'd like to run past you..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I recently had to abandon a project that I already put a"
   lineB$="lot of work into! It would be a shame if it went to waste..."
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,3)
   lineA$="I'd be happy to sell my work on to you for just"
   lineB$="$"+GetFigure$(negPayOff)+" if you think you could do something with it?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=negStar
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    no_projects=no_projects+1 : gamProject=no_projects
    ResetProject(gamProject)
    projGameplay(gamProject)=PercentOf#(hardGameplay(projFormat(gamProject)),charDesign(negChar))
    projReplay(gamProject)=PercentOf#(hardReplay(projFormat(gamProject)),charProgram(negChar))
    proj2D(gamProject)=PercentOf#(hard2D(projFormat(gamProject)),char2D(negChar))
    proj3D(gamProject)=PercentOf#(hard3D(projFormat(gamProject)),char3D(negChar))
    projSound(gamProject)=PercentOf#(hardSound(projFormat(gamProject)),charSound(negChar))
    projMusic(gamProject)=PercentOf#(hardMusic(projFormat(gamProject)),charMusic(negChar))
	projOldIdentity(gamProject)=0 : projIdentity(gamProject)=IdentifyProject(gamProject) 
    For count=1 To 6
     projCredit(gamProject,count)=negChar
    Next
    ChangeRelationship(gamChar,negChar,1) 
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Great, I'm glad the flame I kindled will live on!"
   lineB$="I hope you can steer the project to completion..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, you're not equal to my vision anyway!"
   lineB$="Your incompetence would probably ruin the project..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;16. SELL ASPECT OF WORK
 If negTopic=16
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, not for sale."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+"."
   lineB$="I've heard a lot of good things about your work..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,2)
   lineA$="We're struggling with the "+textAttribute$(negVariable)+" on our"
   lineB$="latest project and could really use your help!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,3)
   lineA$="Would you be willing to sell the work you've been"
   lineB$="doing on "+textAttribute$(negVariable)+" if we paid you $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=negStar
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    If negVariable=1 Then projGameplay(gamProject)=0
    If negVariable=2 Then projReplay(gamProject)=0
    If negVariable=3 Then proj2D(gamProject)=0
    If negVariable=4 Then proj3D(gamProject)=0
    If negVariable=5 Then projSound(gamProject)=0
    If negVariable=6 Then projMusic(gamProject)=0
    projCredit(gamProject,negVariable)=0
    projHiIncrease(gamProject,negVariable)=0
    projLocked(gamProject,negVariable)=0
    ChangeRelationship(gamChar,negChar,1) 
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fantastic! I'm sure it's not easy to surrender your"
   lineB$="hard work but rest assured it will be appreciated..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Your amateur efforts aren't worth buying anyway!"
   lineB$="We'll hire some professionals to do a decent job..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;17. SELL RIGHTS TO PROJECT
 If negTopic=17
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, not for sale."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+"."
   lineB$="I must say that I'm a huge fan of '"+projTitle$(negVariable)+"'!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Would you be willing to sell the rights to it for"
   lineB$="for $"+GetFigure$(negPayOff)+" so that we can make our own version?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    NewChartEntry(2)
    chartGame(2)=projIdentity(negVariable)
    chartChar(2)=negChar : chartCompany(2)=charFed(negChar)
    For v=1 To no_projects
     If projIdentity(v)=projIdentity(negVariable) Then ResetProject(v)
    Next
    If gamRelease=negVariable Then gamRelease=0
    ChangeRelationship(gamChar,negChar,1) 
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Excellent! I'm sure it's not easy to hand over"
   lineB$="your creation but rest assured it's in safe hands..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    NewChartEntry(2)
    chartChar(2)=negChar : chartCompany(2)=charFed(negChar)
    ChangeRelationship(gamChar,negChar,-1) 
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Your amateur efforts aren't worth buying anyway!"
   lineB$="We'll just make our own version of the concept..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;18. SELL MOVIE RIGHTS!
 If negTopic=18
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, not interested."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", it seems that '"+projTitle$(negVariable)+"'"
   lineB$="has been played by some very influential people!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="There's talk of making a movie based on the game"
   lineB$="and they're willing to pay $"+GetFigure$(negPayOff)+" for the rights?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    If gamRelease>0 And projIdentity(gamRelease)=projIdentity(negVariable) Then chartPromotion#(1)=chartPromotion#(1)*2 
    charRep(gamChar)=charRep(gamChar)+PursueValue(charRep(gamChar),99,0)
    For cyc=1 To optTeamSize
     char=projCredit(negVariable,cyc)
     If char>0 And char<>gamChar Then charRep(char)=charRep(char)+PursueValue(charRep(char),99,0)
    Next
    charged=1
   EndIf
   lineA$="A wise decision. Not only is this a license to print money,"
   lineB$="but it will bring your work to a whole new audience as well!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="Are you out of your mind?! What did you get into this"
   lineB$="business for if it wasn't for fame and fortune?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;19. SELL MUSIC RIGHTS!
 If negTopic=19
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, not interested."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", it seems that '"+projTitle$(negVariable)+"'"
   lineB$="has been played by some very influential people!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="The music is so good that a record label is willing"
   lineB$="to pay $"+GetFigure$(negPayOff)+" for the right to release it commercially?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    If gamRelease>0 And projIdentity(gamRelease)=projIdentity(negVariable) Then chartPromotion#(1)=chartPromotion#(1)*2 
    charRep(gamChar)=charRep(gamChar)+PursueValue(charRep(gamChar),99,0)
    char=projCredit(negVariable,6)
    If char>0 And char<>gamChar Then charRep(char)=charRep(char)+PursueValue(charRep(char),99,0)
    charged=1
   EndIf
   lineA$="A wise decision. Not only is this a license to print money,"
   lineB$="but it will bring your work to a whole new audience as well!"
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="Are you out of your mind?! What did you get into this"
   lineB$="business for if it wasn't for fame and fortune?!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;20. SELL EMPLOYEE
 If negTopic=20
  g=GetGender(negVariable)
  optionA$="Yes, accept $"+GetFigure$(negPayOff)+"!" : optionB$="No, we need "+Him$(g)+"."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Hello, I'm "+charName$(negChar)+" from "+fedName$(charFed(negChar))+"."
   lineB$="I'd like to talk to you about your team..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="We've heard a lot of good things about "+charName$(negVariable)
   lineB$="and we're interested in bringing "+Him$(g)+" to "+fedName$(charFed(negChar))+"!"
  EndIf
  If negStage=0 And negTim>675 And negTim<975
   Speak(negVisitor,negStar,3)
   lineA$="Would you be willing to release "+Him$(g)+" from "+Lower$(His$(g))
   lineB$=charContract(negVariable)+"-"+textPeriod$(optTime)+" contract if we paid you $"+GetFigure$(negPayOff)+"?"
  EndIf
  If negStage=0 And negTim>975 Then camFoc=negStar
  If negStage=0 And negTim>1000 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank+negPayOff
    LeaveTeam(negVariable)
    MoveChar(negVariable,charFed(negChar))
    GenerateContract(negVariable)
    charHappiness(negVariable)=charHappiness(negVariable)+Rnd(-1,1)
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negVariable)=charRep(negVariable)+1
    charged=1
   EndIf
   lineA$="I'm glad we could come to an arrangement!"
   lineB$=charName$(negVariable)+" will fit in at "+fedName$(charFed(negChar))+"..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negVariable)=charHappiness(negVariable)+Rnd(-1,1)
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negVariable)=charRep(negVariable)+1
    charged=1
   EndIf
   lineA$="Very well, "+charName$(negVariable)+" isn't worth $"+GetFigure$(negPayOff)
   lineB$="anyway! Losers like you belong together..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf

 ;24. MEMBER ASKS FOR A BREAK
 If negTopic=24
  optionA$="Yes, take a break." : optionB$="No, get to work!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Hi, "+charName$(gamChar)+". Sorry to bother you, but"
   lineB$="I'd like to talk about my work schedule..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I've got a lot going on in my personal life right now"
   lineB$="and could really use some time off to get myself together?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=1
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    ChangeRelationship(gamChar,negChar,1) 
    charVacant(negChar)=1
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Thanks for understanding! Sorry to leave you like"
   lineB$="this, but at least I'll be refreshed when I return..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0 
    ChangeRelationship(gamChar,negChar,-1) 
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, let's just carry on working ourselves into the"
   lineB$="ground! Don't blame me if my work starts to suffer..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;25. HIRE FRIEND
 If negTopic=25
  g=GetGender(negVariable)
  optionA$="Yes, hire "+Him$(g)+"!" : optionB$="No, forget it."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Hi, "+charName$(gamChar)+". Sorry to bother you, but"
   lineB$="I'd like to make a suggestion about the team..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="My friend, "+charName$(negVariable)+", would love to work with us!"
   lineB$="Do you think we could bring "+Him$(g)+" in on a trial basis?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    JoinTeam(negVariable,gamChar)
    charContract(negVariable)=4 : charSalary(negVariable)=charSalary(negVariable)/2
    charHappiness(negChar)=charHappiness(negChar)+1
    charHappiness(negVariable)=charHappiness(negVariable)+1
    charRep(negChar)=charRep(negChar)+1 : charRep(negVariable)=charRep(negVariable)+1
    charRep(gamChar)=charRep(gamChar)+1
    charged=1
   EndIf
   lineA$="Thanks, it'll be great to work with my friend!"
   lineB$="I'm sure "+Lower$(He$(g))+"'ll be an asset to the team too..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1
    charged=1
   EndIf
   lineA$="Why can't we bring our friends onboard?!"
   lineB$="I wish I was free to start my own team..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;26. REMOVE ENEMY
 If negTopic=26
  g=GetGender(negVariable)
  optionA$="Yes, get rid of "+Him$(g)+"!" : optionB$="No, forget it."
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", I'm sick of breathing"
   lineB$="the same air as that asshole, "+charName$(negVariable)+"!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="Nobody likes "+Him$(g)+", so why don't we just get rid of"
   lineB$=Him$(g)+"? I swear to God, if "+Lower$(He$(g))+" doesn't go then I will!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    charNegotiated(negVariable)=1
    LeaveTeam(negVariable)
    charContract(negVariable)=0
    ChangeRelationship(gamChar,negVariable,-1) 
    charHappiness(negVariable)=charHappiness(negVariable)-1
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(negVariable)=charRep(negVariable)-1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="That's great! Sorry to sound like a jerk, but"
   lineB$="the team really will be better off without "+Him$(g)+"..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charVacant(negChar)=1
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(negVariable)=charRep(negVariable)+1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="You're choosing that loser over ME?!"
   lineB$="Fine, let's see how you get on without me..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;27. SEND ENEMY HOME
 If negTopic=27
  g=GetGender(negVariable)
  optionA$="Yes, send "+Him$(g)+" home." : optionB$="No, forget it!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", I'm sick of breathing"
   lineB$="the same air as that asshole, "+charName$(negVariable)+"!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="Can't you send "+Him$(g)+" home for the day?"
   lineB$="I'm sure we'd get a lot more done without "+Him$(g)+"!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    charVacant(negVariable)=1
    ChangeRelationship(gamChar,negVariable,-1) 
    charHappiness(negVariable)=charHappiness(negVariable)-1
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(negVariable)=charRep(negVariable)-1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="That's a relief! "+He$(g)+" was making my life a misery."
   lineB$="It'll be great to have a little breathing space..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charVacant(negChar)=1
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(negVariable)=charRep(negVariable)+1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="You're choosing that loser over ME?!"
   lineB$="Fine, let's see how you get on without me..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;28. MUTUAL TERMINATION
 If negTopic=28
  optionA$="Yes, terminate contract." : optionB$="No, keep working!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Listen, "+charName$(gamChar)+", I think we both know"
   lineB$="that things aren't working out for me here..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I know I've got "+charContract(negChar)+" "+textPeriod$(optTime)+"s left on my contract, but"
   lineB$="wouldn't it be better if we agree to part ways?"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    charNegotiated(negChar)=1
    LeaveTeam(negChar)
    charContract(negChar)=0
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Thanks, that's very gracious of you! Sorry to leave"
   lineB$="you like this, but it's the best thing for both of us..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charVacant(negChar)=1
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Fine, I'll just have to raise hell until you BEG"
   lineB$="me to leave! You can't force me to work for you..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;29. HELD UP FOR MONEY
 If negTopic=29
  optionA$="Yes, pay $"+GetFigure$(negPayOff)+"." : optionB$="No, get to work!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", I'm not sure that you"
   lineB$="appreciate how important I am to this team!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="I'm the star and yet YOU get all the money!"
   lineB$="Give me $"+GetFigure$(negPayOff)+" or I'll take my talents elsewhere..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    PlaySound sCash : gamBank=gamBank-negPayOff
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="That's more like it! Remember how lucky you are"
   lineB$="to work with me and show more respect in future..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    charVacant(negChar)=1 : charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="You don't think I'm worth $"+GetFigure$(negPayOff)+"?"
   lineB$="Fine, let's see how you get on without me!"
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;30. SOCIALIZE WITH COLLEAGUE
 If negTopic=30
  optionA$="Yes, make friends." : optionB$="No, go away!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Nice to meet you, "+charName$(gamChar)+"! I'm "+charName$(negChar)
   lineB$="from "+fedName$(charFed(negChar))+". We work in the same building..."
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Do you want to hang out with us after work?"
   lineB$="I'm sure we've got a lot of ideas to share!"
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    ChangeRelationship(gamChar,negChar,1) 
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Cool, it'll be good to get to know you!"
   lineB$="Who knows? Maybe we'll work together some time..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0 
    ChangeRelationship(gamChar,negChar,-1) 
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Hey, I was only trying to be friendly! You need"
   lineB$="all the connections you can get in this business..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 ;31. GIVE UP FRIEND
 If negTopic=31
  g=GetGender(negVariable)
  optionA$="Yes, give up friend." : optionB$="No, take it easy!"
  If negStage=0 And negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Hey, "+charName$(gamChar)+", why are you friends"
   lineB$="with an asshole like '"+charName$(negVariable)+"'?!"
  EndIf
  If negStage=0 And negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="That loser is damaging our reputation!"
   lineB$="Tell "+Him$(g)+" to get lost or I will..."
  EndIf
  If negStage=0 And negTim>650 Then camFoc=negStar
  If negStage=0 And negTim>675 Then negStage=1 : keytim=20
  ;positive
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=1
   Speak(negVisitor,negStar,3)
   If charged=0
    ChangeRelationship(gamChar,negVariable,-1) 
    ChangeRelationship(negChar,negVariable,-1) 
    ChangeRelationship(gamChar,negChar,1)
    charHappiness(negChar)=charHappiness(negChar)+1
    charRep(gamChar)=charRep(gamChar)-1 : charRep(negChar)=charRep(negChar)+1
    charged=1
   EndIf
   lineA$="Thank God for that! I thought you were really"
   lineB$="friends! We don't need people like that..."
  EndIf
  ;negative
  If negStage=2 And negTim>25 And negTim<325 And negVerdict=-1
   Speak(negVisitor,negStar,1)
   If charged=0
    ChangeRelationship(gamChar,negChar,-1) 
    ChangeRelationship(negChar,negVariable,-1) 
    charHappiness(negChar)=charHappiness(negChar)-1
    charRep(gamChar)=charRep(gamChar)+1 : charRep(negChar)=charRep(negChar)-1
    charged=1
   EndIf
   lineA$="Any friend of "+Lower$(His$(g))+" ISN'T a friend of mine!"
   lineB$="You better reconsider the company you keep..."
  EndIf
  If negStage=2 And negTim>375 Then go=1 
 EndIf
 
 ;-------------------------- MISSION ASSIGNMENTS ---------------------------------
 ;STANDARD INTRO
 If negTopic>100 And negTopic<120 
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   lineA$="Listen, "+charName$(gamChar)+", I've been keeping a close eye"
   lineB$="on your progress and I think changes need to be made..."
  EndIf
 EndIf
 If projIdentified(gamProject)>0 Then projNamer$="'"+projTitle$(gamProject)+"'" Else projNamer$="the latest project" 
 ;1. IMPROVE DESIGN
 If negTopic=101
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(projGameplay(gamProject)+10,hardGameplay(projFormat(gamProject)))
   lineA$="You haven't been putting enough effort into the design"
   lineB$="of "+projNamer$+"! Games are supposed to be fun."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the gameplay to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the project will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last project you ever work on!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;2. IMPROVE PROGRAMMING
 If negTopic=102
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(projReplay(gamProject)+10,hardReplay(projFormat(gamProject)))
   lineA$="You haven't been putting enough effort into the programming"
   lineB$="of "+projNamer$+"! Games should be sophisticated."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the replay value to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the game will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last game you ever make!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;3. IMPROVE 2D GRAPHICS
 If negTopic=103
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(proj2D(gamProject)+10,hard2D(projFormat(gamProject)))
   lineA$="I'm not impressed by the 2D graphics of "+projNamer$+"."
   lineB$="It's important that games look as good as they play!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the 2D graphics to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the game will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last game you ever make!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;4. IMPROVE 3D GRAPHICS
 If negTopic=104
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(proj3D(gamProject)+10,hard3D(projFormat(gamProject)))
   lineA$="I'm not impressed by the 3D graphics of "+projNamer$+"."
   lineB$="It's important that games look as good as they play!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the 3D graphics to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the game will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last game you ever make!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;5. IMPROVE SOUND
 If negTopic=105
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(projSound(gamProject)+10,hardSound(projFormat(gamProject)))
   lineA$="I feel you've overlooked the sound in "+projNamer$+"."
   lineB$="A good gaming experience engages ALL of the senses!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the sound to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the game will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last game you ever make!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;6. IMPROVE MUSIC
 If negTopic=106
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   gamTarget=Rnd(projMusic(gamProject)+10,hardMusic(projFormat(gamProject)))
   lineA$="I haven't heard any decent music for "+projNamer$+"?"
   lineB$="A catchy theme tune gives a game its own identity!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the music to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)
   If fed<no_feds Then lineB$="or the game will never be released by "+fedName$(fed)+"!"
   If fed=no_feds Then lineB$="or it'll be the last game you ever make!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;7. INCREASE TEAM SIZE
 If negTopic=107
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1) : gamTarget=TeamSize(gamChar)+2
   lineA$="What do you expect to achieve with such a small team?"
   lineB$="You need a specialist in each area to make decent games!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Increase the size of your team to "+gamTarget+" people by"
   lineB$=DescribeDate$(gamDeadline,0)+" or you won't belong in this company..."
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf

 ;9. IMPROVE BANK BALANCE
 If negTopic=109
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   If charFed(gamChar)=no_feds Then randy=10 Else randy=100;Rnd(25,100)
   gamTarget=gamBank+(randy*1000)
   If gamTarget<10000 Then gamTarget=10000
   lineA$="I'm not confident about your ability to make money!"
   lineB$="At the end of the day, that's what we're here for..."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Amass a fortune of $"+GetFigure$(gamTarget)+" by "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll make sure you never earn another dollar here!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;10. GET OUT OF DEBT
 If negTopic=110
  If negTim>350 And negTim<650 And charFed(gamChar)<no_feds
   Speak(negVisitor,negStar,1)
   lineA$="Your financial status brings shame on this company!"
   lineB$="Our employees are supposed to be rich - not POOR!"
  EndIf
  If negTim>350 And negTim<650 And charFed(gamChar)=no_feds
   Speak(negVisitor,negStar,1)
   lineA$="You're running on empty! You'll never make it in"
   lineB$="this business if you can't manage your finances."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="You better get out of debt by "+DescribeDate$(gamDeadline,0)
   lineB$="or I'll put you out on the street where you belong!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;11. GET A DEAL
 If negTopic=111
  If negTim>350 And negTim<650
   Speak(negVisitor,negStar,1)
   lineA$="You can't stay independent forever! I've got"
   lineB$="other developers waiting for this opportunity..."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Sign with a major company by "+DescribeDate$(gamDeadline,0)+" or"
   lineB$="we'll have to assume that it's never going to happen!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;12. ACQUIRE CHARACTER
 If negTopic=112
  g=GetGender(gamTarget)
  If negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="Are you aware of a fellow game developer called"
   lineB$="'"+charName$(gamTarget)+"'? I've heard good things about "+Him$(g)+"!"
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Make "+Him$(g)+" part of your team by "+DescribeDate$(gamDeadline,0)
   lineB$="or "+Lower$(He$(g))+"'ll be joining the company as your replacement!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;13. REMOVE CHARACTER
 If negTopic=113
  g=GetGender(gamTarget)
  If negTim>350 And negTim<650
   Speak(negVisitor,negStar,1) : gamDeadline=gamDate+charContract(gamTarget)
   If gamDeadline<gamDate+1 Then gamDeadline=gamDate+1
   lineA$="'"+charName$(gamTarget)+"' is holding your team back!"
   lineB$=He$(g)+" needs to go as soon as possible..."
  EndIf
  If negTim>675 And negTim<975
   Speak(negVisitor,negStar,1)
   lineA$="Get rid of "+Him$(g)+" at the nearest opportunity"
   lineB$="or I'll pull the plug on your entire act!"
  EndIf
  If negTim>1025 Then gamMission=negTopic-100 : go=1
 EndIf
 ;14. RELEASE DEADLINE
 If negTopic=114
  g=GetGender(gamTarget)
  If negTim>350 And negTim<650
   Speak(negVisitor,negStar,1) : gamTarget=gamProject
   lineA$="You can't work on "+projNamer$+" forever! Time is money."
   lineB$="Release it by "+DescribeDate$(gamDeadline,0)+" or I'll release YOU!"
  EndIf
  If negTim>700 Then gamMission=negTopic-100 : go=1
 EndIf
 
 ;MISSION ACCOMPLISHED! (STANDARD)
 If negTopic=120
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Congratulations, "+charName$(gamChar)+"! You followed my"
   lineB$="advice and made your prospects better than ever..."
  EndIf
  If negTim>350 And negTim<650 And charFed(gamChar)<no_feds 
   Speak(negVisitor,negStar,3)
   If charged=0 
    PlaySound sCash : charged=1
    gamBank=gamBank+negPayOff
   EndIf
   lineA$="As a reward for your struggle, here's a $"+GetFigure$(negPayOff)+" bonus!"
   lineB$="I'll still be watching though, so don't get complacent..."
  EndIf
  If negTim>350 And negTim<650 And charFed(gamChar)=no_feds 
   Speak(negVisitor,negStar,3)
   lineA$="I wish I could offer you a reward, but at this"
   lineB$="level experience is the only reward there is!"
  EndIf
  If negTim>700 Then gamMission=0 : go=1
 EndIf
 ;MISSION ACCOMPLISHED! (FINANCIAL)
 If negTopic=121
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   lineA$="Congratulations, "+charName$(gamChar)+"! With $"+GetFigure$(gamBank)
   lineB$="in the bank, the future of your work is safe..."
  EndIf
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,3)
   lineA$="I wish I could offer you a reward, but you're obviously"
   lineB$="capable of earning your own money! Just keep it up..."
  EndIf
  If negTim>700 Then gamMission=0 : go=1
 EndIf
 ;MISSED FAILED (PROFESSIONAL)
 If negTopic=130
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Well, "+charName$(gamChar)+", your time is up and it"
   lineB$="appears that you've failed to follow my advice..."
  EndIf
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   lineA$="Slackers like you don't belong at "+fedName$(charFed(negChar))+"!"
   lineB$="Clean out your office and get out of my sight..."
  EndIf
  If negTim>675 Then camFoc=negStar : pEyes(camFoc)=1
  If negTim>825 Then gamMission=0 : go=1
 EndIf
 ;MISSED FAILED (INDEPENDENT)
 If negTopic=131
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   lineA$="Well, "+charName$(gamChar)+", your time is up and it"
   lineB$="looks like you've failed to deliver the goods..."
  EndIf
  If negTim>350 And negTim<650 
   Speak(negVisitor,negStar,1)
   lineA$="Unfortunately, this is the end of the road for you."
   lineB$="Maybe you'll put more effort into your next big idea..."
  EndIf
  If negTim>675 Then camFoc=negStar : pEyes(camFoc)=1
  If negTim>825 Then gamMission=0 : go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
 ;---------- OPTION BOX ---------------
 If foc=>1 And foc=<2 Then lastfoc=foc
 foc=0
 If negStage=1 And negVerdict=0
  ;reflect mood
  mood=2
  If negTopic=999
   If lastfoc=1 Then mood=1
   If lastfoc=2 Then mood=3
  Else
   If lastfoc=1 Then mood=3
   If lastfoc=2 Then mood=1
  EndIf
  Speak(negStar,negVisitor,mood)
  ;display options
  DrawOption(1,rX#(400),rY#(520),optionA$,"")
  DrawOption(2,rX#(400),rY#(560),optionB$,"")
  ;stat reminder
  If negTopic=1 Then DrawCharProfile(gamChar,rX#(400),rY#(5),1)
  If negTopic=5 Or negTopic=6 Or negTopic=8 Or negTopic=9 Or negTopic=10 Or negTopic=14 Or negTopic=15 Or (negTopic=>28 And negTopic=<30)
   DrawCharProfile(negChar,rX#(400),rY#(5),1)
  EndIf
  If (negTopic=>25 And negTopic=<27) Or negTopic=20 Or negTopic=31
   DrawCharProfile(negVariable,rX#(400),rY#(5),1)
  EndIf
  If negTopic=7 Or negTopic=16 Then DrawProjectProfile(gamProject,rX#(400),rY#(5),1)
  If negTopic=13 Then DrawProjectProfile(gamRelease,rX#(400),rY#(5),1)
  If negTopic=4 Or negTopic=17 Or negTopic=18 Or negTopic=19
   DrawProjectProfile(negVariable,rX#(400),rY#(5),1)
  EndIf
  If negTopic=11 Or negTopic=12
   DrawHardwareProfile(negVariable,rX#(400),rY#(5),1)
  EndIf
 EndIf
 ;return camera
 If negStage=2 And negTim>10 Then camFoc=negVisitor
 ;cursor
 If foc<>oldfoc
  oldfoc=foc
  If foc>0 And gotim>0 Then PlaySound sMenuSelect
 EndIf 
 DrawImage gCursor,MouseX(),MouseY()
 ;mask shaky start
 If gotim=<0 Then Loader("Please Wait","Meeting "+charName$(negChar))

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;restore sound
;ChannelVolume chTheme,1.0	
FreeTimer timer
;free entities
FreeEntity world
FreeEntity cam 
FreeEntity camPivot
FreeEntity dummy
FreeEntity light
FreeEntity lightPivot
;remove workstations
If negSetting=>6 And negSetting=<8
 For cyc=1 To no_desks
  FreeEntity desk(cyc)
 Next
EndIf
;remove characters
For cyc=1 To no_plays
 If pChar(cyc)>0
  FreeEntity p(cyc)
  For limb=1 To 50
   If pShadow(cyc,limb)>0
    FreeEntity pShadow(cyc,limb)
   EndIf
  Next
 EndIf
Next
;leave label
If negTopic=130
 gamAgreement(30+charFed(gamChar))=1
 MoveTeam(gamChar,no_feds)
 GenerateContract(gamChar)
 charRep(gamChar)=charRep(gamChar)-1
 charHappiness(gamChar)=charHappiness(gamChar)-1
EndIf
;check changes
CheckLimits(gamChar)
CheckLimits(negChar)
;update project images
If (negTopic=2 Or negTopic=3 Or negTopic=5) And negVerdict=1
 LoadProjectBox(gamProject)
EndIf
;proceed
screen=20
If negTopic=7 And negVerdict=1 Then gamRelease=gamProject : screen=29
If (negTopic=1 And negVerdict=1) Or negTopic=131 Or negTopic=-1 Then screen=25
End Function

;/////////////////////////////////////////////////////////////////
;---------------------- RELATED FUNCTIONS ------------------------
;/////////////////////////////////////////////////////////////////

;CHANGE RELATIONSHIP
Function ChangeRelationship(char,v,relation)
 charRelationship(char,v)=relation
 charRelationship(v,char)=relation
End Function

;RANDOMIZE MEETING ANIMATIONS
Function RandomizeAnimation(cyc)
 randy=Rnd(0,5000)
 ;seated animations
 If randy=<1 And pAnim(cyc)=>1 And pAnim(cyc)=<10
  Repeat
   pAnim(cyc)=Rnd(1,9) : satisfied=1
   If pAnim(cyc)=<3 And negSetting=1 And cyc<>2 Then satisfied=0
  Until satisfied=1
 EndIf
 ;standing animations
 If randy=<1 And pAnim(cyc)=>20 And pAnim(cyc)=<23 Then pAnim(cyc)=Rnd(20,23) 
 ;activate
 If pAnim(cyc)<>pOldAnim(cyc)
  If pAnim(cyc)=10 Then animSpeed#=Rnd#(0.8,1.3) Else animSpeed#=Rnd#(0.1,0.3)
  Animate p(cyc),1,animSpeed#,pSeq(cyc,pAnim(cyc)),20
  pOldAnim(cyc)=pAnim(cyc)
 EndIf
End Function