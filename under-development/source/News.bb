;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: NEWS REPORTS -------------------
;//////////////////////////////////////////////////////////////////////////////

;------------------------------------------------------------------------
;///////////////////// 22. INTRODUCE NEW WEEK ///////////////////////////
;------------------------------------------------------------------------
Function IntroduceDate()
;advance date
AdvanceDate()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
x1=-150 : x2=850
go=0 : gotim=0 : keytim=0
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames 
	
    ;PORTAL
    gotim=gotim+1
	If gotim>20
	 If KeyDown(1) Or MouseDown(1) Then go=1
	EndIf
	If gotim>500 Then go=1 
	;swing in boxes
	x1=x1+20
	If x1>(rX#(400)-50)-75 Then x1=(rX#(400)-50)-75
	x2=x2-20
	If x2<(rX#(400)-50)+75 Then x2=(rX#(400)-50)+75
	;manage music
	ManageMusic(0) 
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 y=250
 DrawImage gLogo(1),rX#(400),rY#(y)
 DrawImage gMDickie,rX#(400),rY#(530)
 ;date boxes
 If x1<(rX#(400)-50)-75 And x2>(rX#(400)-50)+75
  DrawImage gMenu(1),rX#(x1),rY#(y)+191
  SqueezeFont("New Week",150,18)
  Outline("New Week",rX#(x1),rY#(y)+190,150,150,150,0,0,175)
  DrawImage gMenu(5),rX#(x2),rY#(y)+191
  SqueezeFont(textWeek$(GetWeek(gamDate))+" of "+textMonth$(GetMonth(gamDate)),150,18)
  Outline(textWeek$(GetWeek(gamDate))+" of "+textMonth$(GetMonth(gamDate)),rX#(x2),rY#(y)+190,0,0,75,255,255,255)
 Else
  DrawOption(-1,rX#(400)-50,rY#(y)+191,"New Week",textWeek$(GetWeek(gamDate))+" of "+textMonth$(GetMonth(gamDate)))
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;proceed to charts/reviews
FreeTimer timer
screen=23
End Function

;------------------------------------------------------------------
;///////////////////// ADVANCE DATE PROCESS ///////////////////////
;------------------------------------------------------------------
Function AdvanceDate()
 ;inspire new destinies
 SeedRnd(MilliSecs())
 ;advance date
 gamOldDate=gamDate
 If optTime=2 Then gamDate=gamDate+4 Else gamDate=gamDate+1
 If GetYear(gamDate)<>GetYear(gamOldDate)
  For char=1 To no_chars
   charAge(char)=charAge(char)+1
  Next
 EndIf
 ;expire agreements
 For count=1 To 50
  gamAgreement(count)=gamAgreement(count)-1
  If gamAgreement(count)<0 Then gamAgreement(count)=0
 Next
 ;new office plants
 For cyc=1 To 12
  deskPlant(cyc)=Rnd(1,no_plants)
 Next
 ;CHARACTER DEVELOPMENTS
 For char=1 To no_chars
  ;contract status
  exempt=0
  If gamRelease>0 And gamOldDate=projReleased(gamRelease) And charTeam(char)=gamChar Then exempt=1
  If exempt=0 Then ResetOldValues(char)
  charExperience(char,charFed(char))=charExperience(char,charFed(char))+1
  If charTeam(char)=gamChar Then charExperience(char,0)=charExperience(char,0)+1
  charOldVacant(char)=charVacant(char)
  charVacant(char)=charVacant(char)-1
  If charVacant(char)<0 Then charVacant(char)=0
  If charTeam(char)<>gamChar Or char=fedBooker(charFed(char)) Then charVacant(char)=0
  charContract(char)=charContract(char)-1
  If charContract(char)<0 Then charContract(char)=0
  If charFed(char)=no_feds And charTeam(char)<>gamChar And char<>fedBooker(charFed(char)) Then charContract(char)=0
  charNegotiated(char)=0
  charWorked(char)=0
  ;stat fluctuations
  If char<>gamChar And charTeam(char)<>gamChar
   randy=Rnd(0,10)
   If randy=0 Then charDesign(char)=charDesign(char)+Rnd(-1,1)
   randy=Rnd(0,10)
   If randy=0 Then charProgram(char)=charProgram(char)+Rnd(-1,1)
   randy=Rnd(0,10)
   If randy=0 Then char2D(char)=char2D(char)+Rnd(-1,1)
   randy=Rnd(0,10)
   If randy=0 Then char3D(char)=char3D(char)+Rnd(-1,1)
   randy=Rnd(0,10) 
   If randy=0 Then charSound(char)=charSound(char)+Rnd(-1,1)
   randy=Rnd(0,10)
   If randy=0 Then charMusic(char)=charMusic(char)+Rnd(-1,1) 
   randy=Rnd(0,10)
   If randy=0 Then charRep(char)=charRep(char)+Rnd(-1,1)
  EndIf
  ;develop relationships
  charRelationship(char,char)=0
  For v=1 To no_chars
   If charRelationship(char,v)>0 Then charRelationship(char,v)=charRelationship(char,v)+1
   If charRelationship(char,v)<0 Then charRelationship(char,v)=charRelationship(char,v)-1
   randy=Rnd(0,100-MakePositive#(charRelationship(char,v)))
   If randy=0 And (charRelationship(char,v)>4 Or charRelationship(char,v)<-4) Then charRelationship(char,v)=0
  Next 
  If charTeam(char)<>gamChar Then charHappiness(char)=charHappiness(char)+Rnd(-1,1)
  ;expire agreements
  For count=1 To 50
   charAgreement(char,count)=charAgreement(char,count)-1
   If charAgreement(char,count)<0 Then charAgreement(char,count)=0
  Next
  ;check changes
  CheckLimits(char)
  ResetNewValues(char)
 Next
 ;leading characters
 FindLeadingCharacters()
 ;COMPANY DEVELOPMENTS
 For fed=1 To no_feds
  ;store old values
  fedOldPopularity(fed)=fedPopularity(fed)
  fedOldReputation(fed)=fedReputation(fed)
  ;fluctuate values
  If fed=<4
   randy=Rnd(0,2) : level=Rnd(2,6)
   If randy=0 Then fedPopularity(fed)=fedPopularity(fed)+Rnd(-1,1);PursueValue(fedPopularity(fed),statLevel(level),1)
   randy=Rnd(0,2) : level=Rnd(2,6)
   If randy=0 Then fedReputation(fed)=fedReputation(fed)+Rnd(-1,1);PursueValue(fedReputation(fed),statLevel(level),1)
   ;finances
   factor=Rnd(10,20)
   casher=fedBank(fed)/factor
   casher=RoundOff(casher,1000)
   If casher<1000 Then casher=1000
   randy=Rnd(-2,2)
   If (randy<0 Or fedBank(fed)>2000000) And fedBank(fed)>100000 Then fedBank(fed)=fedBank(fed)-casher
   If (randy>0 Or fedBank(fed)<100000) And fedBank(fed)<2000000 Then fedBank(fed)=fedBank(fed)+casher
   If fedBank(fed)<0 Then fedBank(fed)=0
  EndIf
  ;check limits
  CheckFedLimits(fed)
 Next
 ;HARDWARE DEVELOPMENTS
 FindIndustryStandards()
 For cyc=1 To no_machines
  ;clock old values
  hardOldGameplay(cyc)=hardGameplay(cyc)
  hardOldReplay(cyc)=hardReplay(cyc)
  hardOld2D(cyc)=hard2D(cyc)
  hardOld3D(cyc)=hard3D(cyc)
  hardOldSound(cyc)=hardSound(cyc)
  hardOldMusic(cyc)=hardMusic(cyc)
  ;prepare for new values
  hardNewGameplay(cyc)=hardGameplay(cyc)
  hardNewReplay(cyc)=hardReplay(cyc)
  hardNew2D(cyc)=hard2D(cyc)
  hardNew3D(cyc)=hard3D(cyc)
  hardNewSound(cyc)=hardSound(cyc)
  hardNewMusic(cyc)=hardMusic(cyc)
 Next
 ;PROJECT DEVELOPMENTS
 For cyc=1 To no_projects
  ;clock old values
  projOldGameplay(cyc)=projGameplay(cyc)
  projOldReplay(cyc)=projReplay(cyc)
  projOld2D(cyc)=proj2D(cyc)
  projOld3D(cyc)=proj3D(cyc)
  projOldSound(cyc)=projSound(cyc)
  projOldMusic(cyc)=projMusic(cyc)
  ;prepare for new values
  projNewGameplay(cyc)=projGameplay(cyc)
  projNewReplay(cyc)=projReplay(cyc)
  projNew2D(cyc)=proj2D(cyc)
  projNew3D(cyc)=proj3D(cyc)
  projNewSound(cyc)=projSound(cyc)
  projNewMusic(cyc)=projMusic(cyc)
  ;identification chance
  If projIdentified(cyc)<0 Then projIdentified(cyc)=0 
 Next
End Function

;--------------------------------------------------------------------
;////////////////////// FIND EVENTS FOR NEWS ////////////////////////
;--------------------------------------------------------------------
Function FindEvents()
 ;reset events
 no_events=0
 For count=1 To 100
  eEvent(count)=0 : eStats(count)=0
  eSubject(count)=0 : eRef(count)=0
  eReaction(count)=0 : eUpdated(count)=1
 Next
 ;SCAN ROSTERS FOR EVENTS
For fed=1 To no_feds
 For cyc=1 To fedSize(fed)
  char=fedRoster(fed,cyc)
  charEvent(char)=0 : charUpdated(char)=0
  ;death!!!
  chance=(100-charAge(char))*1000
  randy=Rnd(0,chance)
  If randy=<1 Then AddEvent(char,3,9)
  ;10-30: CONTRACT ISSUES
  If charContract(char)=0 And charTeam(char)<>gamChar
   ;assess chances
   chance=50
   If fed=no_feds And char<>fedBooker(fed)
    chance=(100-GetValue(char))*5
    If fedSize(no_feds)<10 Then chance=chance*2 
    If fedSize(no_feds)>20 Then chance=chance/2
    If chance<10 Then chance=10
   EndIf
   randy=Rnd(0,chance)
   ;renew contract
   If (fed<>no_feds Or char=fedBooker(fed))
    If (randy=<1 And fedSize(fed)<20) Or (randy=<3 And fedSize(fed)<10) Then AddEvent(char,3,11)
   EndIf
   ;move elsewhere
   If fedSize(fed)>1
    If randy=4 Or (randy=5 And fedSize(fed)>20) Then AddEvent(char,3,12)
   EndIf
   ;released
   If fed<>no_feds And fedSize(fed)>1 And fedSize(no_feds)<optRosterLim And char<>fedBooker(fed)
    If (randy=6 And GetValue(char)<70) Or (randy=7 And fedSize(fed)>20) Or (randy=8 And fedSize(no_feds)<20 And GetValue(char)<70)
     AddEvent(char,3,13) 
    EndIf 
   EndIf
  EndIf
  ;return from absence
  If charTeam(char)=gamChar And charVacant(char)=0 And charOldVacant(char)>0 Then AddEvent(char,3,14) 
  ;contract status
  If char=gamChar And charFed(char)<no_feds
   If charContract(char)=1 Then AddEvent(char,3,15) ;yours expires soon
   If charContract(char)=0 Then AddEvent(char,3,16) ;yours expired 
  EndIf 
  If charTeam(char)=gamChar And char<>gamChar
   If charContract(char)=1 Then AddEvent(char,3,17) ;team member expires soon
   If charContract(char)=0 Then AddEvent(char,3,18) ;team member expired 
  EndIf
  If charTeam(char)<>gamChar And charFed(char)<>charFed(gamChar) And charFed(gamChar)<no_feds And charFed(char)<no_feds
   randy=Rnd(0,12)
   If randy=<1 And charContract(char)=1 Then AddEvent(char,3,19) ;poach suggestion
  EndIf
  ;reaction to trades
  If charTradeReaction(char)<>0
   chance=10
   If charTeam(char)=gamChar Or charTeam(MakePositive#(charTradeReaction(char)))=gamChar Then chance=chance/2
   randy=Rnd(0,chance)
   If randy=<1
    If charTradeReaction(char)>0 Then AddEvent(char,3,20) ;happy about move
    If charTradeReaction(char)<0 Then AddEvent(char,3,21) ;unhappy about move
   EndIf
   charTradeReaction(char)=0
  EndIf
  ;30-50: CAREER ISSUES
  ;achieved #1
  If char=chartChar(chartList(1)) And chartOldList(1)<>chartList(1) Then AddEvent(char,3,31) 
  If char=gamChar
   ;achieved millionaire status!
   If gamMillionaire=0 And gamBank=>1000000 Then AddEvent(char,3,32)
   ;new year
   If GetYear(gamDate)<>GetYear(gamOldDate) Then AddEvent(char,3,30)
  EndIf
  ;world leader
  For category=1 To 8
   If char=charLeader(category) And charOldLeader(category)>0 And char<>charOldLeader(category) Then AddEvent(char,3,40+category)
  Next
  ;50-100: PERSONAL ISSUES
  If charTeam(char)=gamChar And char<>gamChar
   ;tardiness
   chance=charRep(char)*4
   randy=Rnd(0,chance)
   If randy=<1 Then AddEvent(char,3,50) ;AWOL
   randy=Rnd(0,100)
   If randy=<1 Then AddEvent(char,3,51) ;ill
   ;react to workmates
   For count=1 To optTeamSize
    member=charRoster(gamChar,count)
    If member>0
     If member<>char And member<>gamChar
      chance=(charRep(char)+charRep(member))*2
      randy=Rnd(0,chance) 
      If randy=0 Or (randy=1 And charRelationship(char,member)<0) Then eRef(no_events+1)=member : AddEvent(char,3,52) ;fall out with team member
      randy=Rnd(0,100) 
      If randy=0 Or (randy=1 And charRelationship(char,member)=0) Then eRef(no_events+1)=member : AddEvent(char,3,53) ;become friends with team member
     EndIf
     chance=100
     If charRelationship(char,member)>0 Then chance=chance/2
     randy=Rnd(0,chance)
     If randy=1 And charDesign(member)>charDesign(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,56) ;taught design
     If randy=2 And charProgram(member)>charProgram(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,57) ;taught Program
     If randy=3 And char2D(member)>char2D(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,58) ;taught 2D
     If randy=4 And char3D(member)>char3D(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,59) ;taught 3D 
     If randy=5 And charSound(member)>charSound(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,60) ;taught Sound
     If randy=6 And charMusic(member)>charMusic(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,61) ;taught Music
     If randy=7 And charRep(member)>charRep(char)+10 Then eRef(no_events+1)=member : AddEvent(char,3,62) ;taught Rep
    EndIf
   Next
  EndIf
  ;external relationships
  If char<>gamChar
   randy=Rnd(0,1500)
   If randy=<1 Then AddEvent(char,3,54) ;rivalry
   randy=Rnd(0,1500)
   If randy=<1 Then AddEvent(char,3,55) ;friendship
  EndIf
  ;spontaneous improvements
  chance=5000
  If charTeam(char)=gamChar Then chance=chance/2
  randy=Rnd(0,chance)
  If randy=1 And charDesign(char)<90 Then AddEvent(char,3,63) ;design
  If randy=2 And charProgram(char)<90 Then AddEvent(char,3,64) ;Program
  If randy=3 And char2D(char)<90 Then AddEvent(char,3,65) ;2D
  If randy=4 And char3D(char)<90 Then AddEvent(char,3,66) ;3D
  If randy=5 And charSound(char)<90 Then AddEvent(char,3,67) ;Sound
  If randy=6 And charMusic(char)<90 Then AddEvent(char,3,68) ;Music
  If randy=7 And charRep(char)<90 Then AddEvent(char,3,69) ;Rep
  If randy=8 Then AddEvent(char,3,70) ;everything!
  ;spontaneous deteriorations
  chance=5000
  If charTeam(char)=gamChar Then chance=chance/2
  randy=Rnd(0,chance)
  If randy=1 And charDesign(char)>50 Then AddEvent(char,3,71) ;design
  If randy=2 And charProgram(char)>50 Then AddEvent(char,3,72) ;Program
  If randy=3 And char2D(char)>50 Then AddEvent(char,3,73) ;2D
  If randy=4 And char3D(char)>50 Then AddEvent(char,3,74) ;3D
  If randy=5 And charSound(char)>50 Then AddEvent(char,3,75) ;Sound
  If randy=6 And charMusic(char)>50 Then AddEvent(char,3,76) ;Music
  If randy=7 And charRep(char)>50 Then AddEvent(char,3,77) ;Rep
  If randy=8 Then AddEvent(char,3,78) ;everything! 
  ;review effects
  If gamRelease>0 And gamOldDate=projReleased(gamRelease)
   If charTeam(char)=gamChar And charRep(char)<>charOldRep(char) Then AddEvent(char,3,79) 
  EndIf
  ;dedication
  If charTeam(char)=gamChar And char<>gamChar
   randy=Rnd(0,75)
   If randy=<1 And charDedicated(char)=0 Then AddEvent(char,3,80) 
   If randy=>2 And randy=<4 And charDedicated(char)>0 Then AddEvent(char,3,81) 
  EndIf
  ;ex-employee
  If charAgreement(char,2)>0
   randy=Rnd(0,6)
   If randy>0 And projCredit(gamProject,randy)=char And projReleased(gamProject)=0 Then eRef(no_events+1)=randy : AddEvent(char,3,82)
  EndIf
  ;bitchiness
  If charTeam(char)<>gamChar
   randy=Rnd(0,2000)
   If randy=<1 And charRep(char)<charRep(gamChar)-10 Then AddEvent(char,3,83) ;inferiority
   If randy=<1 And charRep(char)>charRep(gamChar)+10 Then AddEvent(char,3,84) ;superiority
  EndIf
 Next
Next
 ;1-10: COMPANY ISSUES
 For fed=1 To no_feds
  ;new owner
  randy=Rnd(0,2000)
  If (randy=<1 And fedSize(fed)>1) Or fedBooker(fed)=0 Or charFed(fedBooker(fed))<>fed
   AddEvent(fed,3,1)
   If fed<no_feds
    randy=Rnd(0,10)
    If randy=<2 And fedPolicy(fed)>0 Then AddEvent(fedBooker(fed),3,3) ;abandon policy
    If randy=>3 And randy=<4 And fedPolicy(fed)=0 Then AddEvent(fedBooker(fed),3,4) ;new policy
    If randy=5 And fedPolicy(fed)=0 Then AddEvent(fedBooker(fed),3,2) ;get exclusivity
   EndIf
  EndIf
  ;policies
  randy=Rnd(0,1000)
  If fed<no_feds 
   If randy=0 Or (randy=1 And BrandActive(fedName$(fed))) Then AddEvent(fedBooker(fed),3,2) ;get exclusivity
   If fedPolicy(fed)=1 And BrandActive(fedBrand$(fed))=0 Then AddEvent(fedBooker(fed),3,3) ;must abandon exclusivity
   If randy=>2 And randy=<3 Then AddEvent(fedBooker(fed),3,4) ;new policy
  EndIf
  If ((randy=>4 And randy=<5) Or (fed=no_feds And charFed(gamChar)<>no_feds))
   If fedPolicy(fed)>0 Then AddEvent(fedBooker(fed),3,3) ;abandon policy  
  EndIf
  ;buy talent
  If fed<no_feds And fedSize(fed)<optRosterLim
   randy=Rnd(0,fedSize(fed)*3)
   If randy=0 Or (randy=1 And fedPolicy(fed)=7) Then AddEvent(fed,3,10)  
  EndIf 
 Next
 ;100-200: HARDWARE ISSUES
 For cyc=1 To no_machines
  ;release issues
  date=GetDate(hardWeek(cyc),hardMonth(cyc),hardYear(cyc))
  If hardReleased(cyc)=0
   If gamOldDate<date-24 And gamDate=>date-24
    randy=Rnd(0,3) 
    If CountMachines(2)=<1 Then randy=3
    If randy=<2 Then AddEvent(cyc,2,100) ;first announced
    If randy=3 And optTime=1 Then AddEvent(cyc,2,128) ;brought forward
   EndIf
   If gamOldDate<date-4 And gamDate=>date-4
    randy=Rnd(0,10) 
    If randy=<1 Then AddEvent(cyc,2,102) ;delayed
    If randy=>2 And optTime=1 Then AddEvent(cyc,2,101) ;coming soon
   EndIf
   ;release day
   If gamDate=>date
    AddEvent(cyc,2,103) 
    For fed=1 To no_feds-1
     randy=Rnd(0,50)
     If randy=<1 Or (randy=<10 And fedName$(fed)=hardBrand$(cyc)) Then eRef(no_events+1)=cyc : AddEvent(fedBooker(fed),3,2) ;exclusivity
    Next
    If cyc=3 Then gamFashion=3 : AddEvent(0,0,200) ;amiga makes graphics fashionable
    If cyc=7 Or cyc=28 Then gamFashion=1 : AddEvent(0,0,200) ;nintendo makes gameplay fashionable
    If cyc=11 Or cyc=18 Then gamFashion=2 : AddEvent(0,0,200) ;home computing makes programming fashionable
    If cyc=16 Or cyc=20 Or cyc=28 Then gamFashion=4 : AddEvent(0,0,200) ;playstation makes 3D fashionable
    If cyc=24 Then gamFashion=2 : AddEvent(0,0,200) ;DS makes brain games fashionable
    If cyc=29 Then gamFashion=6 : AddEvent(0,0,200) ;iPhone makes music fashionable
   EndIf
  Else
   ;live issues
   If hardLeader(0)=cyc And hardOldLeader<>cyc Then AddEvent(cyc,2,113) ;new leader!
   If cyc=2 And hardReleased(4)>0 And gamOldDate<hardReleased(4)+4 And gamDate=>hardReleased(4)+4
    AddEvent(cyc,2,104) ;spectrum competes with Amstrad
   EndIf
   If cyc=12 And gamOldDate<date+120 And gamDate=>date+120 Then AddEvent(cyc,2,106) ;Sega releases Mega CD
   If cyc=13 And gamOldDate<date+120 And gamDate=>date+120 Then AddEvent(cyc,2,107) ;Nintendo release Super Gameboy
   If cyc=17 And gamOldDate<date+120 And gamDate=>date+120 Then AddEvent(cyc,2,108) ;N64 expansion pack
   If cyc=26 And hardReleased(28)>0 And gamOldDate<date+48 And gamDate=>date+48 Then AddEvent(cyc,2,109) ;Kinect
   If cyc=27 And hardReleased(28)>0 And gamOldDate<date+48 And gamDate=>date+48 Then AddEvent(cyc,2,109) ;Move
   ;windows tracks industry standards
   If cyc=18
    change=0
    If hardGameplay(cyc)<hardStandard(1)-5 Or (hardReplay(cyc)<hardStandard(2)+5 And hardReplay(cyc)<99) Then change=1 
    If hard2D(cyc)<hardStandard(3)-5 Or hard3D(cyc)<hardStandard(4)-5 Then change=1
    If hardSound(cyc)<hardStandard(5) Or hardMusic(cyc)<hardStandard(6) Then change=1
    If change>0 Then AddEvent(cyc,2,105) 
    If GetYear(gamDate)=2001 And GetYear(gamOldDate)<2001 Then AddEvent(cyc,2,114) ;laptop arrives
   EndIf
   ;mac tracks industry standards
   If cyc=36
    change=0
    If hardGameplay(cyc)<hardStandard(1)-10 Or (hardReplay(cyc)<hardStandard(2)+5 And hardReplay(cyc)<99) Then change=1 
    If hard2D(cyc)<hardStandard(3)-10 Or hard3D(cyc)<hardStandard(4)-10 Then change=1
    If hardSound(cyc)<hardStandard(5)+5 And hardSound(cyc)<99 Then change=1
    If hardMusic(cyc)<hardStandard(6)+5 And hardMusic(cyc)<99 Then change=1
    If change>0 Then AddEvent(cyc,2,105) 
   EndIf
   ;piracy
   If gamDate>date+12 And hardPromotion#(cyc)>-50
    chance=200
    If hardFormat(cyc)=>0 And hardFormat(cyc)=<3 And hardPirated(cyc)=0 Then chance=chance/4
    randy=Rnd(0,chance) 
    If randy=<1 And hardPirated(cyc)=0 Then hardPirated(cyc)=1 : AddEvent(cyc,2,111) ;hacked
    If randy=>2 And randy=<3 And hardPirated(cyc)>0 Then hardPirated(cyc)=0 : AddEvent(cyc,2,112) ;secured
   EndIf
   ;promotion
   If hardPromotion#(cyc)=<0 And hardOldPromotion#(cyc)>0
    AddEvent(cyc,2,110) ;enter decline
    If cyc>1 And hardCost(cyc)>25000 Then hardCost(cyc)=hardCost(cyc)/2 : AddEvent(cyc,2,115) ;automatic price cut 
   EndIf
   If hardPromotion#(cyc)=<-99 And hardOldPromotion#(cyc)>-99 Then AddEvent(cyc,2,119) ;extinct!
   If hardPromotion#(cyc)>-99
    randy=Rnd(0,100)
    If randy=<1 And hardPromotion#(cyc)>0 And hardPromotion#(cyc)<75 Then hardPromotion#(cyc)=hardPromotion#(cyc)+25 : AddEvent(cyc,2,121) ;increase support
    If randy=2 And hardPromotion#(cyc)>25 Then hardPromotion#(cyc)=hardPromotion#(cyc)-25 : AddEvent(cyc,2,122) ;reduce support
    If randy=3 And hardPromotion#(cyc)>50 Then hardPromotion#(cyc)=hardPromotion#(cyc)/2 : AddEvent(cyc,2,123) ;components fail
    If randy=4 And hardPromotion#(cyc)>25 And cyc<>hardLeader(0) Then hardPromotion#(cyc)=hardPromotion#(cyc)/2 : AddEvent(cyc,2,124) ;lack of games
    ;price cuts
    chance=50
    If hardPromotion#(cyc)>0 Then chance=chance*2
    If hardLeader(0)=cyc Then chance=chance*2
    randy=Rnd(0,chance)
    If randy=6 And cyc>1 And hardCost(cyc)>25000 Then hardCost(cyc)=hardCost(cyc)/2 : AddEvent(cyc,2,115) ;SDK 
    If (randy=<3 And hardRetail(cyc)>formatCost(hardFormat(cyc))+1) Or hardRetail(cyc)>formatCost(hardFormat(cyc))+10 Then AddEvent(cyc,2,117) ;retail
    ;price increases
    chance=200
    If hardPromotion#(cyc)>0 Then chance=chance/2
    If hardLeader(0)=cyc Then chance=chance/2
    randy=Rnd(0,chance)
    If randy=6 And cyc>1 Then hardCost(cyc)=hardCost(cyc)+25000 : AddEvent(cyc,2,116) ;SDK
    If (randy=<3 And hardRetail(cyc)<formatCost(hardFormat(cyc))+10) Or hardRetail(cyc)<formatCost(hardFormat(cyc))+2 Then AddEvent(cyc,2,118) ;retail
    ;format costs
    randy=Rnd(0,100)
    If randy=<1 And formatCost(hardFormat(cyc))>0 Then AddEvent(cyc,2,126) ;goes down
    If randy=>2 And randy=<3 And hardRetail(cyc)>formatCost(hardFormat(cyc))+1 Then AddEvent(cyc,2,127) ;goes up
    ;switch to digital
    randy=Rnd(0,100)
    If randy=<1 And GetYear(gamDate)=>2010 And hardFormat(cyc)>0 Then AddEvent(cyc,2,129)
   EndIf
  EndIf
  ;free SDK for employees!
  If hardReleased(cyc)>0 And hardOwned(cyc)=0
   randy=Rnd(0,2)
   If (randy=0 And hardReleased(cyc)=gamDate And fedName$(charFed(gamChar))=hardBrand$(cyc)) Or (fedPolicy(charFed(gamChar))=1 And fedBrand$(charFed(gamChar))=hardBrand$(cyc))
    hardOwned(cyc)=1 : AddEvent(cyc,2,120) 
   EndIf
  EndIf
  ;SDK breaks!
  randy=Rnd(0,200)
  If randy=<1 And hardOwned(cyc)>0 And CountMachines(1)>1 And cyc>1
   hardOwned(cyc)=0
   If gamHardware=cyc
    gamHardware=1
    For v=1 To no_machines
     If hardOwned(v)>0 Then gamHardware=v
    Next
   EndIf
   AddEvent(cyc,2,125)
  EndIf
 Next
 ;200-300: INDUSTRY ISSUES
 ;fashion changes
 If charExperience(gamChar,0)=<1
  gamFashion=1 : AddEvent(0,0,200) 
 Else
  randy=Rnd(0,300)
  If randy=>1 And randy=<6 And gamFashion<>randy Then gamFashion=randy : AddEvent(0,0,200) 
 EndIf
 ;tax changes
 randy=Rnd(0,50)
 If randy=0 And gamTax>20 Then gamTax=gamTax-5 : AddEvent(0,0,201)
 If randy=1 And gamTax<50 Then gamTax=gamTax+5 : AddEvent(0,0,202)
 ;rent changes
 randy=Rnd(0,50)
 If randy=2 And gamRent>500 Then gamRent=gamRent-100 : AddEvent(fedBooker(charFed(gamChar)),3,203)
 If randy=<1 And gamRent<5000 Then gamRent=gamRent+100 : AddEvent(fedBooker(charFed(gamChar)),3,204) 
 ;repairs change
 randy=Rnd(0,50)
 If randy=2 And gamRepairCost>500 Then gamRepairCost=gamRepairCost-250 : AddEvent(0,0,205)
 If randy=<1 And gamRepairCost<10000 Then gamRepairCost=gamRepairCost+250 : AddEvent(0,0,206)  
 ;christmas shopping!
 If GetMonth(gamDate)=12 And (GetWeek(gamDate)=1 Or optTime>1)
  If gamRelease>0
   AddEvent(gamRelease,1,207)
  Else
   AddEvent(0,0,207)
  EndIf
 EndIf
 ;300-400: PROJECT ISSUES
 ;latest release
 If gamRelease>0 And chartPromotion#(1)<10 Then AddEvent(gamRelease,1,300) : gamRelease=0 ;withdrawn from sale
 If gamRelease>0
  randy=Rnd(0,20)
  If charFed(gamChar)<no_feds
   If randy=0 Then chartPromotion#(1)=chartPromotion#(1)+25 : AddEvent(gamRelease,1,306) ;increased support
   If randy=1 And chartPromotion#(1)>35 Then chartPromotion#(1)=chartPromotion#(1)-25 : AddEvent(gamRelease,1,307) ;reduced support
  EndIf
  If chartLimited(1)>0 Then AddEvent(gamRelease,1,301) ;limited by hardware
  ;developer input
  For v=1 To 6
   If projCredit(gamRelease,v)>0 And projCredit(gamRelease,v)<>gamChar
    randy=Rnd(0,200-charRep(projCredit(gamRelease,v)))
    If randy=<1 Then chartPromotion#(1)=chartPromotion#(1)+10 : AddEvent(projCredit(gamRelease,v),3,308) ;good publicity
    randy=Rnd(0,charRep(projCredit(gamRelease,v))*2)
    If randy=<1 And chartPromotion#(1)>20 Then chartPromotion#(1)=chartPromotion#(1)-10 : AddEvent(projCredit(gamRelease,v),3,309) ;bad publicity
   EndIf
  Next
  ;controversial release
  If gamOldDate=projReleased(gamRelease) And softControversial(projIdentity(gamRelease))>0 Then AddEvent(gamRelease,1,303)
  ;piracy
  chance=10
  ;If hardFormat(projFormat(gamRelease))=>0 And hardFormat(projFormat(gamRelease))=<3 Then chance=chance/2
  randy=Rnd(0,chance)
  If randy=<1 And hardPirated(projFormat(gamRelease))>0 And projPirated(gamRelease)=0
   projPirated(gamRelease)=1 : AddEvent(gamRelease,1,302) 
  EndIf
 EndIf
 ;workstations
 For cyc=1 To optTeamSize
  char=charRoster(gamChar,cyc)
  randy=Rnd(0,100-charProgram(char))
  If randy=<1 And char>0 And gamDesk(cyc)=-1 Then gamDesk(cyc)=1 : AddEvent(char,3,318) ;DIY fix
  If char>0 And char<>gamChar And gamDesk(cyc)=-1 And CountDesks()>TeamSize(gamChar) Then AddEvent(cyc,3,305) ;switch to one that works
  If gamDesk(cyc)<>0 And char=0 And fedPolicy(charFed(gamChar))<>9 Then eRef(no_events+1)=cyc : AddEvent(gamChar,3,310) ;empty desks
  randy=Rnd(0,50)
  If randy=<1 And char>0 And gamDesk(cyc)>0 And (gamBank>0 Or char<>gamChar) Then AddEvent(cyc,3,304) ;computer breaks
  If gamDesk(cyc)=-2 And char=0 Then gamDesk(cyc)=1 : eRef(no_events+1)=cyc : AddEvent(0,0,317) ;repair takes effect
  If gamDesk(cyc)=-2 And char>0 Then gamDesk(cyc)=1 : eRef(no_events+1)=cyc : AddEvent(char,3,317) ;repair takes effect
 Next
 ;enforce hardware policy
 fed=charFed(gamChar)
 If fedPolicy(fed)=1 And hardBrand$(gamHardware)<>fedBrand$(fed) And hardBrand$(gamHardware)<>fedName$(fed)
  gamNewHardware=gamHardware : hi=0
  For v=1 To no_machines
   If hardBrand$(v)=fedBrand$(fed) Or hardBrand$(v)=fedName$(fed)
    If hardReleased(v)>hi And hardOwned(v)>0 Then gamNewHardware=v : hi=hardReleased(v)
   EndIf
  Next
  If gamNewHardware<>gamHardware Then eRef(no_events+1)=gamHardware : gamHardware=gamNewHardware : AddEvent(gamHardware,2,319)
 EndIf
 ;400+: mission reminders
 If gamMission=>1 And gamMission=<6 Then AddEvent(gamProject,1,400+gamMission) 
 If gamMission=>7 And gamMission=<11 Then AddEvent(gamChar,3,400+gamMission)  
 If gamMission=12 Or gamMission=13 Then AddEvent(gamTarget,3,400+gamMission) 
 If gamMission=14 Then AddEvent(gamTarget,1,400+gamMission) 
 ;verify character changes
 For char=1 To no_chars
  CheckLimits(char)
  CheckNewValues(char)
 Next
 ;nothing to report!
 If no_events=0 Then AddEvent(0,0,0) 
End Function

;--------------------------------------------------------------------
;//////////////////// ADD AN EVENT TO THE LIST //////////////////////
;--------------------------------------------------------------------
Function AddEvent(cyc,stats,event) ;0=industry, 1=project, 2=hardware, 3=character, 4=company
 If no_events<100
  ;increment list
  no_events=no_events+1
  eSubject(no_events)=cyc
  eEvent(no_events)=event
  eStats(no_events)=stats
  char=cyc
  ;newspaper images
  eNewspaper(no_events)=Rnd(1,1)
  eAdvert(no_events)=Rnd(8,14) : negFormat=-1
  If eAdvert(no_events)=<9 Then eAdvert(no_events)=Rnd(1,9) 
  eScene(no_events,1)=Rnd(1,3)
  Repeat
   eScene(no_events,2)=Rnd(1,3)
  Until eScene(no_events,2)<>eScene(no_events,1)
  ;COMPANY ISSUES
  ;new booker
  If event=1
   level=90 : its=0
   Repeat
    its=its+1 : satisfied=1
    newbie=fedRoster(fed,Rnd(1,fedSize(fed)))
    randy=Rnd(0,1)
    If randy=0 And charRep(newbie)<level And charAge(newbie)<level Then satisfied=0 
    If newbie=fedBooker(fed) Then satisfied=0
    If newbie=gamChar Then satisfied=0
    If its>100 And level>10 Then level=level-10 : its=0 
   Until satisfied=1 Or its>1000
   char=newbie : eSubject(no_events)=newbie
   fedBooker(fed)=char
   If fed=charFed(gamChar) Then gamMission=0
   charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),99,0)
   If charHappiness(char)<0 Then charHappiness(char)=0
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;exclusivity policy
  If event=2
   If eRef(no_events)>0
    fedBrand$(fed)=hardBrand$(eRef(no_events))
   Else
    If BrandActive(fedName$(fed))
     fedBrand$(fed)=fedName$(fed)
    Else
     its=0
     Repeat
      brand$=hardBrand$(Rnd(1,no_machines)) : its=its+1
     Until (BrandActive(brand$)>0 And brand$<>fedBrand$(fed)) Or its>1000
     fedBrand$(fed)=brand$
    EndIf
   EndIf
   fedPolicy(fed)=1
   If charFed(gamChar)=fed And hardBrand$(gamHardware)<>fedBrand$(fed)
    For v=1 To no_machines
     If hardOwned(v)>0 And hardBrand$(v)=fedBrand$(fed)
      gamHardware=v
      If projReleased(gamProject)=0 Then projFormat(gamProject)=v
     EndIf
    Next
   EndIf
   charNewRep(char)=charNewRep(char)+1 : eUpdated(no_events)=0
  EndIf
  ;abandon policy
  If event=3
   eRef(no_events)=fedPolicy(fed) : fedPolicy(fed)=0
   charNewRep(char)=charNewRep(char)-1 : eUpdated(no_events)=0
  EndIf
  ;new policy
  If event=4
   Repeat
    policy=Rnd(2,no_policies)
   Until policy<>fedPolicy(fed)
   fedPolicy(fed)=policy
   charNewRep(char)=charNewRep(char)+1 : eUpdated(no_events)=0
  EndIf
  ;CONTRACT ISSUES
  ;dead!!!
  If event=9
   eRef(no_events)=charFed(char) 
   LeaveTeam(char)
   MoveChar(char,0)
   ;charNewDesign(char)=0 : charNewProgram(char)=0
   ;charNew2D(char)=0 : charNew3D(char)=0
   ;charNewSound(char)=0 : charNewMusic(char)=0
   eUpdated(no_events)=0
  EndIf
  ;bought
  If event=10 
   its=0
   Repeat
    satisfied=1 : its=its+1
    char=Rnd(1,no_chars)
    If char=gamChar Or charTeam(char)=gamChar Then satisfied=0
    If charFed(char)=fed Or charFed(char)=no_feds Or charFed(char)=0 Then satisfied=0
    If fedSize(charFed(char))<10 Then satisfied=0
    If charEvent(char)>0 Then satisfied=0
   Until satisfied=1 Or its>1000
   eRef(no_events)=charFed(char)
   MoveChar(char,fed) : GenerateContract(char)
   charSalary(char)=PercentOf#(charSalary(char),150)
   charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),99,0)
   If charHappiness(char)<0 Then charHappiness(char)=0
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
   eSubject(no_events)=char
   eStats(no_events)=3
  EndIf
  ;renew contract
  If event=11 
   GenerateContract(char)
   charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;move elsewhere
  If event=12 
   eRef(no_events)=charFed(char)
   Repeat
    newbie=Rnd(1,no_feds-1) : satisfied=1
    randy=Rnd(0,1)
    If (randy=0 And fedSize(newbie)>18) Or fedSize(newbie)=>optRosterLim Then satisfied=0
    If newbie=fed Then satisfied=0
   Until satisfied=1
   MoveChar(char,newbie)
   GenerateContract(char)
   charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;released
  If event=13
   eRef(no_events)=charFed(char)
   MoveChar(char,no_feds)
   GenerateContract(char)
   charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),25,0)
   charHappiness(char)=charHappiness(char)-1 
   eUpdated(no_events)=0
  EndIf
  ;happy about move
  If event=20
   eRef(no_events)=charTradeReaction(char)
   charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;unhappy about move
  If event=21
   eRef(no_events)=MakePositive#(charTradeReaction(char))
   charNewRep(char)=charNewRep(char)-1
   charHappiness(char)=charHappiness(char)-1
   eUpdated(no_events)=0
  EndIf
  ;CAREER ISSUES
  ;new year
  If event=30 Then charNewRep(char)=charNewRep(char)+1 : eUpdated(no_events)=0
  ;number one!
  If event=31 
   charNewRep(char)=charNewRep(char)+1;PursueValue(charRep(char),99,0)
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
   For count=1 To optTeamSize
    member=charRoster(char,count)
    If char<>member
     charNewRep(member)=charNewRep(member)+1
     charHappiness(member)=charHappiness(member)+1
     CheckLimits(member)
    EndIf
   Next 
  EndIf
  ;millionaire!
  If event=32
   gamMillionaire=1
   charNewRep(char)=charNewRep(char)+1;PursueValue(charRep(char),99,0)
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf 
  ;world leader
  If event=>41 And event=<48
   If event<>47 Then charNewRep(char)=charNewRep(char)+1;PursueValue(charRep(char),99,0)
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;PERSONAL ISSUES
  ;absent
  If event=50 Or event=51
   charVacant(char)=1
   If event=50 Then charNewRep(char)=charNewRep(char)-1 : eUpdated(no_events)=0
   charHappiness(char)=charHappiness(char)-1
  EndIf
  ;fall out with team member
  If event=52
   v=eRef(no_events)
   ChangeRelationship(char,v,-1)
   charVacant(char)=1 ;: charVacant(v)=1
   charNewRep(char)=charNewRep(char)-1 : charRep(v)=charRep(v)-1 
   charHappiness(char)=charHappiness(char)-1 : charHappiness(v)=charHappiness(v)-1
   eUpdated(no_events)=0
  EndIf
  ;befriend team member
  If event=53
   v=eRef(no_events)
   ChangeRelationship(char,v,1)
   charNewRep(char)=charNewRep(char)+1 : charRep(v)=charRep(v)+1 
   charHappiness(char)=charHappiness(char)+1 : charHappiness(v)=charHappiness(v)+1
   eUpdated(no_events)=0
  EndIf
  ;external rivalry
  If event=54
   Repeat 
    newbie=Rnd(1,no_chars)
   Until newbie<>char And charTeam(newbie)<>gamChar And charFed(newbie)>0
   eRef(no_events)=newbie
   ChangeRelationship(char,newbie,-1)
   charNewRep(char)=charNewRep(char)-1 : charRep(newbie)=charRep(newbie)-1 
   charHappiness(char)=charHappiness(char)-1 : charHappiness(newbie)=charHappiness(newbie)-1
   eUpdated(no_events)=0
  EndIf
  ;external friendship
  If event=55
   Repeat 
    newbie=Rnd(1,no_chars)
   Until newbie<>char And charTeam(newbie)<>gamChar And charFed(newbie)>0
   eRef(no_events)=newbie
   ChangeRelationship(char,newbie,1)
   charNewRep(char)=charNewRep(char)+1 : charRep(newbie)=charRep(newbie)+1 
   charHappiness(char)=charHappiness(char)+1 : charHappiness(newbie)=charHappiness(newbie)+1
   eUpdated(no_events)=0
  EndIf
  ;learn from colleague
  If event=>56 And event=<62
   v=eRef(no_events)
   ChangeRelationship(char,v,1)
   If event=56 Then charNewDesign(char)=charNewDesign(char)+PursueValue(charDesign(char),charDesign(v),0)
   If event=57 Then charNewProgram(char)=charNewProgram(char)+PursueValue(charProgram(char),charProgram(v),0)
   If event=58 Then charNew2D(char)=charNew2D(char)+PursueValue(char2D(char),char2D(v),0)
   If event=59 Then charNew3D(char)=charNew3D(char)+PursueValue(char3D(char),char3D(v),0)
   If event=60 Then charNewSound(char)=charNewSound(char)+PursueValue(charSound(char),charSound(v),0)
   If event=61 Then charNewMusic(char)=charNewMusic(char)+PursueValue(charMusic(char),charMusic(v),0)
   If event=62 Then charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),charRep(v),1)
   If event<>62 Then charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;spontaneous stat improvements
  If event=>63 And event=<70
   If event=63 Or event=70 Then charNewDesign(char)=charNewDesign(char)+PursueValue(charDesign(char),99,200)
   If event=64 Or event=70 Then charNewProgram(char)=charNewProgram(char)+PursueValue(charProgram(char),99,200)
   If event=65 Or event=70 Then charNew2D(char)=charNew2D(char)+PursueValue(char2D(char),99,200)
   If event=66 Or event=70 Then charNew3D(char)=charNew3D(char)+PursueValue(char3D(char),99,200)
   If event=67 Or event=70 Then charNewSound(char)=charNewSound(char)+PursueValue(charSound(char),99,200)
   If event=68 Or event=70 Then charNewMusic(char)=charNewMusic(char)+PursueValue(charMusic(char),99,200)
   If event=69 Or event=70 Then charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),99,200)
   If event<69 Then charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf
  ;spontaneous stat deterioration
  If event=>71 And event=<78
   If event=71 Or event=78 Then charNewDesign(char)=charNewDesign(char)+PursueValue(charDesign(char),25,200)
   If event=72 Or event=78 Then charNewProgram(char)=charNewProgram(char)+PursueValue(charProgram(char),25,200)
   If event=73 Or event=78 Then charNew2D(char)=charNew2D(char)+PursueValue(char2D(char),25,200)
   If event=74 Or event=78 Then charNew3D(char)=charNew3D(char)+PursueValue(char3D(char),25,200)
   If event=75 Or event=78 Then charNewSound(char)=charNewSound(char)+PursueValue(charSound(char),25,200)
   If event=76 Or event=78 Then charNewMusic(char)=charNewMusic(char)+PursueValue(charMusic(char),25,200)
   If event=77 Or event=78 Then charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),25,200)
   If event<77 Then charNewRep(char)=charNewRep(char)-1
   charHappiness(char)=charHappiness(char)-1
   eUpdated(no_events)=0
  EndIf
  ;dedication
  If event=80
   charDedicated(char)=Rnd(1,6)
   If charDedicated(char)=1 Then charNewDesign(char)=charNewDesign(char)+1
   If charDedicated(char)=2 Then charNewProgram(char)=charNewProgram(char)+1
   If charDedicated(char)=3 Then charNew2D(char)=charNew2D(char)+1
   If charDedicated(char)=4 Then charNew3D(char)=charNew3D(char)+1
   If charDedicated(char)=5 Then charNewSound(char)=charNewSound(char)+1
   If charDedicated(char)=6 Then charNewMusic(char)=charNewMusic(char)+1
   charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf 
  If event=81
   If charDedicated(char)=1 Then charNewDesign(char)=charNewDesign(char)-1
   If charDedicated(char)=2 Then charNewProgram(char)=charNewProgram(char)-1
   If charDedicated(char)=3 Then charNew2D(char)=charNew2D(char)-1
   If charDedicated(char)=4 Then charNew3D(char)=charNew3D(char)-1
   If charDedicated(char)=5 Then charNewSound(char)=charNewSound(char)-1
   If charDedicated(char)=6 Then charNewMusic(char)=charNewMusic(char)-1
   eRef(no_events)=charDedicated(char) : charDedicated(char)=0
   charNewRep(char)=charNewRep(char)-1
   charHappiness(char)=charHappiness(char)-1
   eUpdated(no_events)=0
  EndIf 
  ;ex-employee takes work!
  If event=82
   If eRef(no_events)=1 Then projGameplay(gamProject)=0
   If eRef(no_events)=2 Then projReplay(gamProject)=0
   If eRef(no_events)=3 Then proj2D(gamProject)=0
   If eRef(no_events)=4 Then proj3D(gamProject)=0
   If eRef(no_events)=5 Then projSound(gamProject)=0
   If eRef(no_events)=6 Then projMusic(gamProject)=0
   projLocked(gamProject,eRef(no_events))=0
   ChangeRelationship(gamChar,char,-1)
   charNewRep(char)=charNewRep(char)+PursueValue(charRep(char),25,0)
   eUpdated(no_events)=0
  EndIf
  ;bitchiness
  If event=83 Or event=84
   ChangeRelationship(gamChar,char,-1)
   charRep(gamChar)=charRep(gamChar)-1
   charNewRep(char)=charNewRep(char)-1
   charHappiness(char)=charHappiness(char)-1
   eUpdated(no_events)=0
  EndIf 
  ;HARDWARE ISSUES
  ;delayed
  If event=102
   date=GetDate(hardWeek(cyc),hardMonth(cyc),hardYear(cyc)) : extend=Rnd(4,12)
   hardWeek(cyc)=GetWeek(date+extend) : hardMonth(cyc)=GetMonth(date+extend) : hardYear(cyc)=GetYear(date+extend)
  EndIf
  ;released
  If event=103
   hardWeek(cyc)=GetWeek(gamDate) : hardMonth(cyc)=GetMonth(gamDate) : hardYear(cyc)=GetYear(gamDate)
   hardReleased(cyc)=gamDate : hardUsers(cyc)=GetHardwareSales(cyc) : hardPromotion#(cyc)=Rnd(50,100)
   If cyc=18 Or hardBrand$(cyc)="Apple" Then hardPromotion#(cyc)=hardPromotion#(cyc)+25
  EndIf
  ;spectrum keeps up
  If event=104
   hardNewGameplay(cyc)=hardGameplay(4) : hardNewReplay(cyc)=hardReplay(4)
   hardNewSound(cyc)=hardSound(4) : hardNewMusic(cyc)=hardMusic(4)
   If hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0 
   eUpdated(no_events)=0
  EndIf
  ;windows adapts
  If event=105
   If cyc=36 ;mac
    hardNewGameplay(cyc)=hardStandard(1)-10 : hardNewReplay(cyc)=hardStandard(2)+5
    hardNew2D(cyc)=hardStandard(3)-10 : hardNew3D(cyc)=hardStandard(4)-10
    hardNewSound(cyc)=hardStandard(5)+5 : hardNewMusic(cyc)=hardStandard(6)+5
   Else ;PC
    hardNewGameplay(cyc)=hardStandard(1)-5 : hardNewReplay(cyc)=hardStandard(2)+5
    hardNew2D(cyc)=hardStandard(3)-5 : hardNew3D(cyc)=hardStandard(4)-5
    hardNewSound(cyc)=hardStandard(5) : hardNewMusic(cyc)=hardStandard(6)
   EndIf
   If hardNewReplay(cyc)>99 Then hardNewReplay(cyc)=99
   If hardNewSound(cyc)>99 Then hardNewSound(cyc)=99 
   If hardNewMusic(cyc)>99 Then hardNewMusic(cyc)=99 
   hardPromotion#(cyc)=hardPromotion#(cyc)+10
   If hardPromotion#(cyc)<10 Then hardPromotion#(cyc)=10
   eUpdated(no_events)=0
  EndIf
  ;Mega CD
  If event=106
   hardFormat(cyc)=1 : hardRetail(cyc)=hardRetail(cyc)-5
   hardNewGameplay(cyc)=hardGameplay(cyc)+5 : hardNewReplay(cyc)=hardReplay(cyc)+5
   hardNew2D(cyc)=hard2D(cyc)+10 : hardNew3D(cyc)=hard3D(cyc)+10
   hardNewSound(cyc)=hardSound(cyc)+10 : hardNewMusic(cyc)=hardMusic(cyc)+10
   If hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0 
   eUpdated(no_events)=0
  EndIf
  ;super gameboy
  If event=107
   combine=hardUsers(7)+hardUsers(13)
   hardUsers(7)=combine : hardPeakUsers(7)=combine
   If hardPromotion#(7)<0 Then hardPromotion#(7)=0 
   hardUsers(13)=combine : hardPeakUsers(13)=combine 
   If hardPromotion#(13)<0 Then hardPromotion#(13)=0 
  EndIf
  ;N64 expansion pack
  If event=108
   hardNew2D(cyc)=hard2D(cyc)+5 : hardNew3D(cyc)=hard3D(cyc)+5
   If hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0 
   eUpdated(no_events)=0
  EndIf
  ;Kinect & Move
  If event=109
   hardNewGameplay(cyc)=hardGameplay(cyc)+5
   hardPromotion#(cyc)=hardPromotion#(cyc)+25 
   eUpdated(no_events)=0
  EndIf
  ;laptop computers
  If event=114
   hardPromotion#(cyc)=hardPromotion#(cyc)+10
   If hardPromotion#(cyc)<10 Then hardPromotion#(cyc)=10 
  EndIf
  ;retail goes down
  If event=117
   ;If hardRetail(cyc)>formatCost(hardFormat(cyc))+10 Then hardRetail(cyc)=hardRetail(cyc)-5 Else hardRetail(cyc)=hardRetail(cyc)-1
   hardRetail(cyc)=hardRetail(cyc)-1
   If hardRetail(cyc)<formatCost(hardFormat(cyc))+1 Then hardRetail(cyc)=formatCost(hardFormat(cyc))+1 
  EndIf
  ;retail goes up
  If event=118
   hardRetail(cyc)=hardRetail(cyc)+1
   If hardRetail(cyc)>30 Then hardRetail(cyc)=30
  EndIf
  ;format cost goes down
  If event=126
   formatCost(hardFormat(cyc))=formatCost(hardFormat(cyc))-1
   If formatCost(hardFormat(cyc))<1 Then formatCost(hardFormat(cyc))=1
   For v=1 To no_machines
    If hardRetail(v)<formatCost(hardFormat(v))+1 Then hardRetail(v)=formatCost(hardFormat(v))+1 
   Next
  EndIf
  ;format cost goes up
  If event=127
   formatCost(hardFormat(cyc))=formatCost(hardFormat(cyc))+1
   For v=1 To no_machines
    If hardRetail(v)<formatCost(hardFormat(v))+1 Then hardRetail(v)=formatCost(hardFormat(v))+1 
   Next
  EndIf
  ;release brought forward
  If event=128
   date=GetDate(hardWeek(cyc),hardMonth(cyc),hardYear(cyc)) : extend=Rnd(4,12)
   hardWeek(cyc)=GetWeek(date-extend) : hardMonth(cyc)=GetMonth(date-extend) : hardYear(cyc)=GetYear(date-extend)
  EndIf
  ;switch to digital
  If event=129
   hardFormat(cyc)=0
   If hardRetail(cyc)>5 Then hardRetail(cyc)=5
   If hardPromotion#(cyc)<0 Then hardPromotion#(cyc)=0 
  EndIf
  ;PROJECT ISSUES
  ;controversial release
  If event=303
   chartPromotion#(1)=chartPromotion#(1)+25
   For count=1 To optTeamSize
    char=charRoster(gamChar,count)
    charRep(char)=charRep(char)+Rnd(-1,1);PursueValue(charRep(char),99,0)
    charHappiness(char)=charHappiness(char)+Rnd(-1,1)
   Next
  EndIf
  ;workstation breaks
  If event=304
   char=charRoster(gamChar,cyc) : eSubject(no_events)=char
   charHappiness(char)=charHappiness(char)-1
   gamDesk(cyc)=-1
   For count=1 To 2
    randy=Rnd(1,6)
    If randy=>1 And randy=<6 And projReleased(gamProject)=0
     change=0
     If randy=1 And projCredit(gamProject,randy)=char Then projNewGameplay(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=1
     If randy=2 And projCredit(gamProject,randy)=char Then projNewReplay(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=2
     If randy=3 And projCredit(gamProject,randy)=char Then projNew2D(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=3
     If randy=4 And projCredit(gamProject,randy)=char Then projNew3D(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=4
     If randy=5 And projCredit(gamProject,randy)=char Then projNewSound(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=5
     If randy=6 And projCredit(gamProject,randy)=char Then projNewMusic(gamProject)=0 : projLocked(gamProject,randy)=0 : projCredit(gamProject,randy)=0 : change=6
     If change>0 And eEvent(no_events)<310
      eEvent(no_events)=310+change : eSubject(no_events)=gamProject
      eStats(no_events)=1 : eRef(no_events)=char
      eUpdated(no_events)=0
     EndIf
    EndIf
   Next
  EndIf
  ;switch to new workstation
  If event=305
   eSubject(no_events)=charRoster(gamChar,cyc)
   For count=1 To optTeamSize
    If count<>cyc And gamDesk(count)>0 And charRoster(gamChar,count)=0
     charRoster(gamChar,count)=charRoster(gamChar,cyc)
     charRoster(gamChar,cyc)=0
     eRef(no_events)=count
     Exit
    EndIf 
   Next
  EndIf
  ;developer brings good publicity
  If event=308 Then charNewRep(char)=charNewRep(char)+1 : eUpdated(no_events)=0
  ;developer brings bad publicity
  If event=309 Then charNewRep(char)=charNewRep(char)-1 : eUpdated(no_events)=0
  ;fixed computer
  If event=318
   charNewRep(char)=charNewRep(char)+1
   charHappiness(char)=charHappiness(char)+1
   eUpdated(no_events)=0
  EndIf 
  ;PREPARE TO INDICATE CHANGES
  If char>0
   CheckNewValues(char)
  EndIf
 EndIf
End Function

;----------------------------------------------------------------------
;/////////////////////// 23. NEWS PROCESS /////////////////////////////
;----------------------------------------------------------------------
Function NewsReports()
;advance date
AdvanceDate()
;prepare events
FindEvents()
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
cyc=1
go=0 : gotim=-10 : keytim=0
While go=0
 
 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;counters
	keytim=keytim-1
	If keytim<1 Then keytim=0 
	
	;PROCESS
    gotim=gotim+1
	If gotim>20 And keytim=0 
	 ;alter project stats
	 If eStats(cyc)=1 And eUpdated(cyc)=0
	  v=eSubject(cyc)
	  If projGameplay(v)<projNewGameplay(v) Then projGameplay(v)=projGameplay(v)+1 : keytim=5
	  If projGameplay(v)>projNewGameplay(v) Then projGameplay(v)=projGameplay(v)-1 : keytim=5
	  If projReplay(v)<projNewReplay(v) Then projReplay(v)=projReplay(v)+1 :  keytim=5
	  If projReplay(v)>projNewReplay(v) Then projReplay(v)=projReplay(v)-1 :  keytim=5
	  If proj2D(v)<projNew2D(v) Then proj2D(v)=proj2D(v)+1 :  keytim=5
	  If proj2D(v)>projNew2D(v) Then proj2D(v)=proj2D(v)-1 :  keytim=5
	  If proj3D(v)<projNew3D(v) Then proj3D(v)=proj3D(v)+1 :  keytim=5
	  If proj3D(v)>projNew3D(v) Then proj3D(v)=proj3D(v)-1 :  keytim=5
	  If projSound(v)<projNewSound(v) Then projSound(v)=projSound(v)+1 :  keytim=5
	  If projSound(v)>projNewSound(v) Then projSound(v)=projSound(v)-1 :  keytim=5
	  If projMusic(v)<projNewMusic(v) Then projMusic(v)=projMusic(v)+1 :  keytim=5
	  If projMusic(v)>projNewMusic(v) Then projMusic(v)=projMusic(v)-1 :  keytim=5
	  If projGameplay(v)=projNewGameplay(v) And projReplay(v)=projNewReplay(v)
	   If proj2D(v)=projNew2D(v) And proj3D(v)=projNew3D(v)
	    If projSound(v)=projNewSound(v) And projMusic(v)=projNewMusic(v) Then eUpdated(cyc)=1
	   EndIf
	  EndIf
	 EndIf
	 ;alter hardware stats
	 If eStats(cyc)=2 And eUpdated(cyc)=0
	  v=eSubject(cyc)
	  If hardGameplay(v)<hardNewGameplay(v) Then hardGameplay(v)=hardGameplay(v)+1 :  keytim=5
	  If hardGameplay(v)>hardNewGameplay(v) Then hardGameplay(v)=hardGameplay(v)-1 :  keytim=5
	  If hardReplay(v)<hardNewReplay(v) Then hardReplay(v)=hardReplay(v)+1 :  keytim=5
	  If hardReplay(v)>hardNewReplay(v) Then hardReplay(v)=hardReplay(v)-1 :  keytim=5
	  If hard2D(v)<hardNew2D(v) Then hard2D(v)=hard2D(v)+1 :  keytim=5
	  If hard2D(v)>hardNew2D(v) Then hard2D(v)=hard2D(v)-1 :  keytim=5
	  If hard3D(v)<hardNew3D(v) Then hard3D(v)=hard3D(v)+1 :  keytim=5
	  If hard3D(v)>hardNew3D(v) Then hard3D(v)=hard3D(v)-1 :  keytim=5
	  If hardSound(v)<hardNewSound(v) Then hardSound(v)=hardSound(v)+1 :  keytim=5
	  If hardSound(v)>hardNewSound(v) Then hardSound(v)=hardSound(v)-1 :  keytim=5
	  If hardMusic(v)<hardNewMusic(v) Then hardMusic(v)=hardMusic(v)+1 :  keytim=5
	  If hardMusic(v)>hardNewMusic(v) Then hardMusic(v)=hardMusic(v)-1 :  keytim=5
	  If hardGameplay(v)=hardNewGameplay(v) And hardReplay(v)=hardNewReplay(v)
	   If hard2D(v)=hardNew2D(v) And hard3D(v)=hardNew3D(v)
	    If hardSound(v)=hardNewSound(v) And hardMusic(v)=hardNewMusic(v) Then eUpdated(cyc)=1
	   EndIf
	  EndIf
	 EndIf 
	 ;alter character stats
	 If eStats(cyc)=3 And eUpdated(cyc)=0
	  char=eSubject(cyc)
	  If charDesign(char)<charNewDesign(char) Then charDesign(char)=charDesign(char)+1 :  keytim=5
	  If charDesign(char)>charNewDesign(char) Then charDesign(char)=charDesign(char)-1 :  keytim=5
	  If charProgram(char)<charNewProgram(char) Then charProgram(char)=charProgram(char)+1 :  keytim=5
	  If charProgram(char)>charNewProgram(char) Then charProgram(char)=charProgram(char)-1 :  keytim=5
	  If char2D(char)<charNew2D(char) Then char2D(char)=char2D(char)+1 :  keytim=5
	  If char2D(char)>charNew2D(char) Then char2D(char)=char2D(char)-1 :  keytim=5
	  If char3D(char)<charNew3D(char) Then char3D(char)=char3D(char)+1 :  keytim=5
	  If char3D(char)>charNew3D(char) Then char3D(char)=char3D(char)-1 :  keytim=5
	  If charSound(char)<charNewSound(char) Then charSound(char)=charSound(char)+1 :  keytim=5
	  If charSound(char)>charNewSound(char) Then charSound(char)=charSound(char)-1 :  keytim=5
	  If charMusic(char)<charNewMusic(char) Then charMusic(char)=charMusic(char)+1 :  keytim=5
	  If charMusic(char)>charNewMusic(char) Then charMusic(char)=charMusic(char)-1 :  keytim=5
	  If charRep(char)<charNewRep(char) Then charRep(char)=charRep(char)+1 :  keytim=5
	  If charRep(char)>charNewRep(char) Then charRep(char)=charRep(char)-1 :  keytim=5
	  If charDesign(char)=charNewDesign(char) And charProgram(char)=charNewProgram(char)
	   If char2D(char)=charNew2D(char) And char3D(char)=charNew3D(char)
	    If charSound(char)=charNewSound(char) And charMusic(char)=charNewMusic(char)
	     If charRep(char)=charNewRep(char) Then eUpdated(cyc)=1
	    EndIf
	   EndIf
	  EndIf
	 EndIf
	 ;browse events
	 If KeyDown(1) Or KeyDown(28) Or MouseDown(1)
	  If gotim>20 And eUpdated(cyc)>0
	   PlaySound sMenuSelect : gotim=-10
	   cyc=cyc+1 : keytim=10
	   If cyc>no_events Then cyc=no_events : go=1
	  EndIf
	 EndIf 
	EndIf 
	;paper impact
	If gotim=0 Then ProduceSound(0,sPaper,22050,0.25)
	;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300) 
 ;CONSTRUCT NEWSPAPER
 If gotim>0
  ;base image
  x=rX#(400) : y=GraphicsHeight()-294
  DisplayMagazine(cyc,x,y)
  ;get context
  char=eSubject(cyc) : refChar=0
  g=GetGender(char)
  SetFont fontNews(5) : Color 0,0,0
  newsX=x+5 : newsY=y+200 : spacing=40
  ;default message
  headline$="Event #"+eEvent(cyc)
  If eEvent(cyc)=0
   headline$="CALM BEFORE THE STORM"
   Text newsX,newsY,"Nothing has happened in the world of gaming.",1,1
  EndIf
  ;company issues
  If eEvent(cyc)=1
   headline$="NEW KING IN TOWN"
   Text newsX,newsY-(spacing/2),charName$(char)+" has been appointed as",1,1
   Text newsX,newsY+(spacing/2),"the new head of "+fedName$(charFed(char))+".",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=2
   headline$="COMPANY POLICY"
   Text newsX,newsY-(spacing/2),fedName$(charFed(char))+" have adopted a new policy of",1,1
   Text newsX,newsY+(spacing/2),"only making games for "+fedBrand$(charFed(char))+" products.",1,1
  EndIf 
  If eEvent(cyc)=3
   headline$="NO POLICY"
   Text newsX,newsY-(spacing/2),fedName$(charFed(char))+" have abandoned their policy of",1,1
   If eRef(cyc)=1 Then Text newsX,newsY+(spacing/2),"only making games for "+fedBrand$(charFed(char))+" products.",1,1
   If eRef(cyc)>1 Then Text newsX,newsY+(spacing/2),textPolicy$(eRef(cyc))+".",1,1
  EndIf 
  If eEvent(cyc)=4
   headline$="COMPANY POLICY"
   Text newsX,newsY-(spacing/2),fedName$(charFed(char))+" have adopted a new policy of",1,1
   Text newsX,newsY+(spacing/2),textPolicy$(fedPolicy(charFed(char)))+".",1,1
  EndIf 
  ;contract issues
  If eEvent(cyc)=9
   headline$="GAME OVER"
   Text newsX,newsY-(spacing/2),charName$(char)+" has died at the age of "+charAge(char)+".",1,1
   Text newsX,newsY+(spacing/2),He$(g)+" lives on through the classic games "+Lower$(He$(g))+" made...",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : PlaySound sGrumble : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=10
   headline$="ALL YOUR TALENT ARE BELONG TO US"
   Text newsX,newsY-(spacing/2),fedName$(charFed(char))+" have poached "+charName$(char)+" from",1,1
   Text newsX,newsY+(spacing/2),fedName$(eRef(cyc))+" in a deal worth $"+getFigure$(charSalary(char))+" per "+textPeriod$(optTime)+"!",1,1
  EndIf 
  If eEvent(cyc)=11
   headline$="THE SAGA CONTINUES"
   Text newsX,newsY-(spacing/2),charName$(char)+" has renewed "+Lower$(His$(g))+" contract with "+fedName$(charFed(char)),1,1
   Text newsX,newsY+(spacing/2),"and will remain at the company for the next "+charContract(char)+" "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=12 And eRef(cyc)<no_feds
   headline$="MOVING ON"
   Text newsX,newsY-(spacing/2),charName$(char)+" has left "+fedName$(eRef(cyc)),1,1
   Text newsX,newsY+(spacing/2),"to pursue a career at "+fedName$(charFed(char))+".",1,1
  EndIf
  If eEvent(cyc)=12 And eRef(cyc)=no_feds
   headline$="TURNING PRO"
   Text newsX,newsY-(spacing/2),"The independent developer "+charName$(char),1,1
   Text newsX,newsY+(spacing/2),"has managed to find employment at "+fedName$(charFed(char))+".",1,1
  EndIf
  If eEvent(cyc)=13
   headline$="BACK TO BASICS"
   Text newsX,newsY-(spacing/2),charName$(char)+" has been released from "+fedName$(eRef(cyc)),1,1
   Text newsX,newsY+(spacing/2),"and must make ends meet on the independent scene.",1,1
  EndIf
  If eEvent(cyc)=14
   headline$="BACK TO BUSINESS"
   Text newsX,newsY-(spacing/2),charName$(char)+" has returned from "+Lower$(His$(g))+" absence",1,1
   Text newsX,newsY+(spacing/2),"and will once again be available to work.",1,1
  EndIf
  If eEvent(cyc)=15
   headline$="THE END IS NEAR"
   Text newsX,newsY-(spacing/2),"Your contract with "+fedName$(charFed(char))+" expires next "+textPeriod$(optTime)+"!",1,1
   Text newsX,newsY+(spacing/2),"It may be time to re-evaluate your future.",1,1
  EndIf
  If eEvent(cyc)=16
   headline$="JUDGMENT DAY"
   Text newsX,newsY-(spacing/2),"Your contract with "+fedName$(charFed(char))+" has expired!",1,1
   Text newsX,newsY+(spacing/2),"There'll be a meeting later to discuss your future.",1,1
  EndIf
  If eEvent(cyc)=17
   headline$="THE END IS NEAR"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s contract expires next "+textPeriod$(optTime)+"!",1,1
   Text newsX,newsY+(spacing/2),"It may be time to re-evaluate "+Lower$(His$(g))+" future.",1,1
  EndIf
  If eEvent(cyc)=18
   headline$="JUDGMENT DAY"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s contract has expired! There'll",1,1
   Text newsX,newsY+(spacing/2),"be a meeting later to discuss "+Lower$(His$(g))+" future.",1,1
  EndIf
  If eEvent(cyc)=19
   headline$="NOW OR NEVER"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s contract expires soon!",1,1
   Text newsX,newsY+(spacing/2),"Now would be an ideal time to poach "+Him$(g)+".",1,1
  EndIf
  If eEvent(cyc)=20
   If charFed(eRef(cyc))=charFed(char)
    headline$="WE MEET AGAIN"
    Text newsX,newsY-(spacing/2),charName$(char)+" is pleased that "+charName$(eRef(cyc)),1,1
    Text newsX,newsY+(spacing/2),"has joined "+fedName$(charFed(char))+"!",1,1
   Else
    headline$="GOOD RIDDANCE"
    Text newsX,newsY-(spacing/2),charName$(char)+" is glad that "+charName$(eRef(cyc)),1,1
    Text newsX,newsY+(spacing/2),"has left "+fedName$(charFed(char))+"!",1,1
   EndIf
   refChar=eRef(cyc)
  EndIf
  If eEvent(cyc)=21
   If charFed(eRef(cyc))=charFed(char)
    headline$="ENEMY AT THE GATE"
    Text newsX,newsY-(spacing/2),charName$(char)+" is upset that "+charName$(eRef(cyc)),1,1
    Text newsX,newsY+(spacing/2),"has joined "+fedName$(charFed(char))+".",1,1
   Else
    headline$="WISH YOU WERE HERE"
    Text newsX,newsY-(spacing/2),charName$(char)+" is sad that "+charName$(eRef(cyc)),1,1
    Text newsX,newsY+(spacing/2),"has left "+fedName$(charFed(char))+".",1,1
   EndIf
   refChar=eRef(cyc)
  EndIf
  ;career issues
  If eEvent(cyc)=30
   headline$="HAPPY NEW YEAR!"
   Text newsX,newsY-(spacing/2),"A new year is upon us! You are now "+charAge(char)+" years old and",1,1
   Text newsX,newsY+(spacing/2),"have been in the games industry for "+CountExperience(char,0)+" "+textPeriod$(optTime)+"s.",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=31
   headline$="TOP OF THE POPS"
   namer$=softTitle$(chartGame(chartList(1)))
   If softDated(chartGame(chartList(1)))>0 Then namer$=namer$+" "+GetYear(gamDate)
   Text newsX,newsY-(spacing/2),"Congratulations to "+charName$(char)+" for taking",1,1
   Text newsX,newsY+(spacing/2),"'"+namer$+"' to the top of the charts!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=32
   headline$="SHOW ME THE MONEY"
   Text newsX,newsY-(spacing/2),"Congratulations on becoming a millionaire! After "+CountExperience(char,0)+" "+textPeriod$(optTime)+"s",1,1
   Text newsX,newsY+(spacing/2),"in the business, you've amassed a fortune of $"+GetFigure$(gamBank)+".",1,1 
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  ;world leaders
  If eEvent(cyc)=>41 And eEvent(cyc)=<48
   headline$="BEST IN THE WORLD"
   Text newsX,newsY-(spacing/2),charName$(char)+" has overtaken "+charName$(charOldLeader(eEvent(cyc)-40))+" as",1,1
   If eEvent(cyc)=41 Then Text newsX,newsY+(spacing/2),"the best game designer in the world!",1,1
   If eEvent(cyc)=42 Then Text newsX,newsY+(spacing/2),"the best programmer in the industry!",1,1
   If eEvent(cyc)=43 Then Text newsX,newsY+(spacing/2),"the best 2D artist in the industry!",1,1
   If eEvent(cyc)=44 Then Text newsX,newsY+(spacing/2),"the best 3D artist in the industry!",1,1
   If eEvent(cyc)=45 Then Text newsX,newsY+(spacing/2),"the best sound engineer in the industry!",1,1
   If eEvent(cyc)=46 Then Text newsX,newsY+(spacing/2),"the best musician in the industry!",1,1
   If eEvent(cyc)=47 Then Text newsX,newsY+(spacing/2),"the world's most famous game developer!",1,1
   If eEvent(cyc)=48 Then Text newsX,newsY+(spacing/2),"the industry's highest paid game developer!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf 
  ;personal issues
  If eEvent(cyc)=50
   headline$="MISSING IN ACTION"
   Text newsX,newsY-(spacing/2),charName$(char)+" has failed to turn up!",1,1
   Text newsX,newsY+(spacing/2),He$(g)+" won't be around to contribute.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=51
   headline$="UNDER THE WEATHER"
   Text newsX,newsY-(spacing/2),charName$(char)+" has been taken ill and won't",1,1
   Text newsX,newsY+(spacing/2),"be able to work until "+Lower$(He$(g))+" feels better.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=52
   headline$="SPACE INVADERS"
   Text newsX,newsY-PercentOf#(spacing,80),charName$(char)+" has fallen out with "+charName$(eRef(cyc)),1,1
   Text newsX,newsY                       ,"while working on the latest project! "+He$(g)+" won't be",1,1
   Text newsX,newsY+PercentOf#(spacing,80),"available to work until the issue is resolved...",1,1
   If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=53
   headline$="MULTI-PLAYER"
   Text newsX,newsY-(spacing/2),charName$(char)+" has developed a good relationship with",1,1
   Text newsX,newsY+(spacing/2),charName$(eRef(cyc))+" while working on the latest project!",1,1
  EndIf
  If eEvent(cyc)=54
   headline$="ENEMY WAVE"
   Text newsX,newsY-(spacing/2),charName$(char)+" has developed a bitter rivalry",1,1
   Text newsX,newsY+(spacing/2),"with "+charName$(eRef(cyc))+" of "+fedName$(charFed(eRef(cyc)))+".",1,1
  EndIf 
  If eEvent(cyc)=55
   headline$="TEAM PLAY"
   Text newsX,newsY-(spacing/2),charName$(char)+" has developed a good relationship",1,1
   Text newsX,newsY+(spacing/2),"with "+charName$(eRef(cyc))+" of "+fedName$(charFed(eRef(cyc)))+".",1,1
  EndIf
  ;learn from colleagues
  If eEvent(cyc)=>52 And eEvent(cyc)=<62 Then refChar=eRef(cyc)
  If eEvent(cyc)=>56 And eEvent(cyc)=<62
   headline$="ROLE MODEL"
   If eEvent(cyc)=56
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" game design skills!",1,1
   EndIf
   If eEvent(cyc)=57
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" programming skills!",1,1
   EndIf
   If eEvent(cyc)=58
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" 2D graphics!",1,1
   EndIf
   If eEvent(cyc)=59
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" 3D graphics!",1,1
   EndIf
   If eEvent(cyc)=60
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" sound engineering!",1,1
   EndIf
   If eEvent(cyc)=61
    Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+" has been helping "+charName$(char),1,1
    Text newsX,newsY+(spacing/2),"to improve "+Lower$(His$(g))+" music skills!",1,1
   EndIf
   If eEvent(cyc)=62
    Text newsX,newsY-(spacing/2),charName$(char)+"'s reputation has benefited",1,1
    Text newsX,newsY+(spacing/2),"from being associated with "+charName$(eRef(cyc))+"!",1,1
   EndIf
  EndIf
  ;spontaneous improvements
  If eEvent(cyc)=63
   headline$="GRAND DESIGNS"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s game design skills have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf
  If eEvent(cyc)=64
   headline$="BREAKING THE CODE"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s programming skills have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf
  If eEvent(cyc)=65
   headline$="2D OR NOT 2D"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s 2D graphics have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf
  If eEvent(cyc)=66
   headline$="3-DIMENSIONAL CHARACTER"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s 3D graphics have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf
  If eEvent(cyc)=67
   headline$="SOUNDING GOOD"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s sound effects have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf
  If eEvent(cyc)=68
   headline$="MUSIC TO THE EAR"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s music skills have",1,1
   Text newsX,newsY+(spacing/2),"improved considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf 
  If eEvent(cyc)=69
   headline$="THE NEXT BIG THING"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s profile has been raised",1,1
   Text newsX,newsY+(spacing/2),"considerably after being hyped by the press!",1,1
  EndIf
  If eEvent(cyc)=70
   headline$="SELF-IMPROVEMENT"
   Text newsX,newsY-(spacing/2),charName$(char)+" has been working hard",1,1
   Text newsX,newsY+(spacing/2),"to improve every aspect of "+Lower$(His$(g))+" work!",1,1
  EndIf
  ;spontaneous deteriorations
  If eEvent(cyc)=71
   headline$="DESIGNED TO FAIL"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s game design skills have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=72
   headline$="BROKEN CODE"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s programming skills have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=73
   headline$="2D OR NOT 2D"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s 2D graphics have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=74
   headline$="BACK TO THE DRAWING BOARD"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s 3D graphics have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=75
   headline$="SOUND CLASH"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s sound effects have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s.",1,1
  EndIf
  If eEvent(cyc)=76
   headline$="OUT OF TUNE"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s music skills have",1,1
   Text newsX,newsY+(spacing/2),"deteriorated considerably in recent "+textPeriod$(optTime)+"s!",1,1
  EndIf 
  If eEvent(cyc)=77
   headline$="LAUGHING STOCK"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s reputation has suffered",1,1
   Text newsX,newsY+(spacing/2),"after being ridiculed by the press.",1,1
  EndIf
  If eEvent(cyc)=78
   headline$="SELF-DESTRUCTION"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s complacency has negatively",1,1
   Text newsX,newsY+(spacing/2),"affected every aspect of "+Lower$(His$(g))+" work.",1,1
  EndIf
  If eEvent(cyc)=79 And charRep(char)>charOldRep(char)
   headline$="RAVE REVIEWS"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s profile has been raised by",1,1
   Text newsX,newsY+(spacing/2),Lower$(His$(g))+" involvement in the release of '"+projTitle$(gamRelease)+"'!",1,1
  EndIf
  If eEvent(cyc)=79 And charRep(char)<charOldRep(char)
   headline$="THE WEAKEST LINK"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s reputation has been damaged by",1,1
   Text newsX,newsY+(spacing/2),Lower$(His$(g))+" involvement in the release of '"+projTitle$(gamRelease)+"'.",1,1
  EndIf
  If eEvent(cyc)=80
   headline$="TUNNEL VISION"
   Text newsX,newsY-(spacing/2),charName$(char)+" suggested in a recent interview that",1,1
   Text newsX,newsY+(spacing/2),Lower$(He$(g))+" considers "+Him$(g)+"self to be a dedicated "+textRole$(charDedicated(char))+".",1,1
  EndIf
  If eEvent(cyc)=81
   headline$="DEAD-ICATION"
   Text newsX,newsY-(spacing/2),charName$(char)+" conceded in a recent interview that "+Lower$(He$(g))+" no",1,1
   Text newsX,newsY+(spacing/2),"longer considers "+Him$(g)+"self to be a dedicated "+textRole$(eRef(cyc))+".",1,1
  EndIf
  If eEvent(cyc)=82
   headline$="THE ENEMY WITHIN"
   Text newsX,newsY-(spacing/2),"After being released, "+charName$(char)+" left with all of",1,1
   Text newsX,newsY+(spacing/2),"the work "+Lower$(He$(g))+" did on the "+textAttribute$(eRef(cyc))+" of '"+projTitle$(gamProject)+"'!",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=83
   headline$="INFERIORITY COMPLEX"
   Text newsX,newsY-(spacing/2),"The inferior developer, "+charName$(char)+",",1,1
   Text newsX,newsY+(spacing/2),"has been publicly criticizing your work.",1,1
  EndIf
  If eEvent(cyc)=84
   headline$="SUPERIORITY COMPLEX"
   Text newsX,newsY-(spacing/2),"The superior developer, "+charName$(char)+",",1,1
   Text newsX,newsY+(spacing/2),"has been publicly criticizing your work.",1,1
  EndIf
  ;hardware issues
  If eEvent(cyc)=100
   headline$="COMING SOON" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have announced the release of the",1,1
   Text newsX,newsY+(spacing/2),"'"+hardTitle$(v)+"' in "+textMonth$(hardMonth(v))+" "+hardYear(v)+"!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=101
   headline$="COMING SOON" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" are preparing to release",1,1
   Text newsX,newsY+(spacing/2),"the '"+hardTitle$(v)+"' next month!",1,1
  EndIf
  If eEvent(cyc)=102
   headline$="DELAYED GRATIFICATION" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have delayed the release of the",1,1
   Text newsX,newsY+(spacing/2),"'"+hardTitle$(v)+"' until "+textMonth$(hardMonth(v))+" "+hardYear(v)+".",1,1
   If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=103
   headline$="LATEST ARRIVAL" : v=eSubject(cyc)
   Text newsX,newsY-PercentOf#(spacing,80),hardBrand$(v)+" have released the '"+hardTitle$(v)+"', which",1,1
   description$="promises to revolutionize the world of gaming!"
   If v=2 Then description$="promises to make gaming more sophisticated!"
   If v=3 Then description$="boasts the best graphics of any home system!"
   If v=4 Then description$="offers an affordable alternative to the C64!"
   If v=5 Then description$="boasts the best gameplay of any home system!"
   If v=6 Then description$="boasts the best graphics of any home system!"
   If v=7 Then description$="puts quality gaming in the palm of your hand!"
   If v=8 Then description$="offers the best 2D graphics of any handheld!"
   If v=9 Then description$="boasts the best 3D graphics of any handheld!"
   If v=10 Then description$="promises to make gaming more sophisticated!"
   If v=11 Then description$="promises to be superior to the Atari ST!"
   If v=12 Then description$="promises to revolutionize the world of gaming!"
   If v=13 Then description$="is superior to the Megadrive in every way!"
   If v=14 Then description$="is the world's first 64-bit console!"
   If v=15 Then description$="boasts the best 3D graphics of any home system!"
   If v=16 Then description$="is their first foray into the console market!"
   If v=17 Then description$="promises the best 3D gameplay ever experienced!"
   If v=18 Then description$="promises to adapt with advances in technology!"
   If v=19 Then description$="boasts the best 3D graphics of any home system!"
   If v=20 Then description$="hopes to continue the success of the first version!"
   If v=21 Then description$="promises the best gameplay of any home console!"
   If v=22 Then description$="is their first foray into the console market!" 
   If v=23 Then description$="promises the best 2D gaming of any handheld!"
   If v=24 Then description$="promises to revolutionize handheld gaming!"
   If v=25 Then description$="puts a Playstation in the palm of your hand!"
   If v=26 Then description$="hopes to continue the success of the first version!"
   If v=27 Then description$="boasts the best 3D graphics gaming has ever seen!"
   If v=28 Then description$="promises to revolutionize the world of gaming!"
   If v=29 Then description$="has exposed a whole new audience to gaming!"
   If v=30 Then description$="promises to revolutionize the way we live!"
   If v=31 Then description$="boasts the best 3D graphics of any handheld!"
   If v=32 Then description$="boasts the sharpest screen of any handheld!"
   If v=33 Then description$="promises to revolutionize the world of gaming!"
   If v=34 Then description$="promises to bring gaming to perfection!"
   If v=35 Then description$="promises to be the final word in gaming!"
   If v=36 Then description$="promises to revolutionize home computing!"
   If v=37 Then description$="brings arcade quality games into the home!"
   If v=38 Then description$="boasts superior gameplay to the computer!"
   Text newsX,newsY,description$,1,1
   Text newsX,newsY+PercentOf#(spacing,80),"A software development kit will cost $"+GetFigure$(hardCost(v))+".",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=104
   headline$="KEEPING UP APPEARANCES" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have re-released the "+hardTitle$(v)+" to",1,1
   Text newsX,newsY+(spacing/2),"compete with the "+hardTitle$(4)+" and "+hardTitle$(3)+".",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=105
   headline$="KEEPING UP APPEARANCES" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have adapted the "+hardTitle$(v)+" to",1,1
   Text newsX,newsY+(spacing/2),"meet the standards set by consoles.",1,1
  EndIf
  If eEvent(cyc)=106
   headline$="POWER UP" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"Sega have released the 'Mega CD' add-on to",1,1
   Text newsX,newsY+(spacing/2),"squeeze extra power out of the Megadrive!",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=107
   headline$="UNITED FRONT" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"Nintendo have released the 'Super Gameboy' to combine",1,1
   Text newsX,newsY+(spacing/2),"the audiences of the Gameboy and the Super Nintendo!",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=108
   headline$="POWER UP" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"Nintendo have released an 'Expansion Pack' to",1,1
   Text newsX,newsY+(spacing/2),"increase the graphical capabilities of the N64!",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=109
   headline$="STOLEN MOVES" : v=eSubject(cyc)  
   If eSubject(cyc)=26 Then Text newsX,newsY-(spacing/2),"Microsoft have released 'Kinect' for the X-Box 360",1,1
   If eSubject(cyc)=27 Then Text newsX,newsY-(spacing/2),"Sony have released their 'Playstation Move' device",1,1
   Text newsX,newsY+(spacing/2),"to compete with the innovations of the Nintendo Wii!",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=110
   headline$="OVER THE HILL" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The '"+hardTitle$(v)+"' has peaked at "+GetFigure$(hardPeakUsers(v))+" users",1,1
   Text newsX,newsY+(spacing/2),"and looks set to decline over the coming years.",1,1
  EndIf
  If eEvent(cyc)=111
   headline$="PIRACY THREAT" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The '"+hardTitle$(v)+"' has been hacked! Software",1,1
   Text newsX,newsY+(spacing/2),"released on the system now risks being pirated.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=112
   headline$="PIRATES AT BAY" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have introduced security measures to prevent",1,1
   Text newsX,newsY+(spacing/2),"software for the '"+hardTitle$(v)+"' from being pirated.",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=113
   headline$="NEW KING IN TOWN" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The '"+hardTitle$(v)+"' has overtaken the '"+hardTitle$(hardOldLeader)+"'",1,1
   Text newsX,newsY+(spacing/2),"as the world's leading platform with "+GetFigure$(hardUsers(v))+" users.",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=114
   headline$="THE LAP OF POWER" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The 'Laptop' computer has been unveiled, which",1,1
   Text newsX,newsY+(spacing/2),"promises to bring home computing to the masses!",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=115
   headline$="GOING CHEAP" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have slashed the price of the",1,1
   Text newsX,newsY+(spacing/2),"'"+hardTitle$(v)+"' to $"+GetFigure$(hardCost(v))+" to encourage uptake!",1,1
  EndIf
  If eEvent(cyc)=116
   headline$="PRICE HIKE" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have increased the price of the",1,1
   Text newsX,newsY+(spacing/2),"'"+hardTitle$(v)+"' to $"+GetFigure$(hardCost(v))+" to boost profits.",1,1
  EndIf
  If eEvent(cyc)=117
   headline$="PRICE CUTS" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have reduced the recommended retail",1,1
   Text newsX,newsY+(spacing/2),"price of "+hardTitle$(v)+" games to $"+hardRetail(v)+".",1,1
  EndIf
  If eEvent(cyc)=118
   headline$="PRICE HIKE" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have raised the recommended retail",1,1
   Text newsX,newsY+(spacing/2),"price of "+hardTitle$(v)+" games to $"+hardRetail(v)+".",1,1
  EndIf
  If eEvent(cyc)=119
   headline$="OLD NEWS" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The '"+hardTitle$(v)+"' has become an obsolete platform",1,1
   Text newsX,newsY+(spacing/2),"that is only attended to by "+GetFigure$(hardUsers(v))+" diehard fans.",1,1
  EndIf
  If eEvent(cyc)=120
   headline$="FAMILY BUSINESS" : v=eSubject(cyc)
   If hardBrand$(v)=fedName$(charFed(gamChar)) Then namer$="employee" Else namer$="associate"
   Text newsX,newsY-(spacing/2),"As an "+namer$+" of "+hardBrand$(v)+", you have been provided",1,1
   Text newsX,newsY+(spacing/2),"with a complementary '"+hardTitle$(v)+"' worth $"+GetFigure$(hardCost(v))+"!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=121
   headline$="TIPPED FOR SUCCESS" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have invested more money in the promotion",1,1
   Text newsX,newsY+(spacing/2),"of the "+hardTitle$(v)+" to help make it a success!",1,1 
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=122
   headline$="HANDS OFF THE WHEEL" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have reduced the amount of promotion",1,1
   Text newsX,newsY+(spacing/2),"that they were putting into the "+hardTitle$(v)+".",1,1 
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=123
   headline$="SHORT CIRCUIT" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The reputation of the "+hardTitle$(v)+" has been",1,1
   Text newsX,newsY+(spacing/2),"tarnished by complaints about faulty components!",1,1 
   If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=124
   headline$="ALONE IN THE DARK" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The "+hardTitle$(v)+" is thought to be suffering from",1,1
   Text newsX,newsY+(spacing/2),"a lack of support from major game developers.",1,1 
   If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=125
   headline$="DAMAGED GOODS" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"Your '"+hardTitle$(v)+"' has stopped working! You'll have",1,1
   Text newsX,newsY+(spacing/2),"to pay $"+GetFigure$(hardCost(v))+" for another software development kit.",1,1 
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=126
   headline$="PRICE CUTS" : v=eSubject(cyc)
   If hardFormat(v)=0 Then Text newsX,newsY-(spacing/2),"The cost of making data available to download has fallen",1,1
   If hardFormat(v)>0 Then Text newsX,newsY-(spacing/2),"The cost of manufacturing "+Lower$(formatName$(hardFormat(v)))+"s has fallen",1,1
   Text newsX,newsY+(spacing/2),"to $"+formatCost(hardFormat(v))+", resulting in larger profit margins for games!",1,1 
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=127
   headline$="PRICE HIKE" : v=eSubject(cyc)
   If hardFormat(v)=0 Then Text newsX,newsY-(spacing/2),"The cost of making data available to download has risen",1,1
   If hardFormat(v)>0 Then Text newsX,newsY-(spacing/2),"The cost of manufacturing "+Lower$(formatName$(hardFormat(v)))+"s has risen",1,1
   Text newsX,newsY+(spacing/2),"to $"+formatCost(hardFormat(v))+", which squeezes the profit margin for games.",1,1 
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=128
   headline$="COMING SOON" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),hardBrand$(v)+" have brought forward the release",1,1
   Text newsX,newsY+(spacing/2),"of the '"+hardTitle$(v)+"' to "+textMonth$(hardMonth(v))+" "+hardYear(v)+"!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=129
   headline$="THE DIGITAL AGE" : v=eSubject(cyc)
   Text newsX,newsY,"The "+hardTitle$(v)+" has switched to the '"+formatName$(hardFormat(v))+"' format.",1,1
   If eReaction(cyc)=0 Then PlaySound sMilestone : eReaction(cyc)=1
  EndIf
  ;industry issues
  If eEvent(cyc)=200
   headline$="FOLLOW THE FASHION"
   Text newsX,newsY-(spacing/2),"The latest market research suggests that",1,1
   If gamFashion=1 Then Text newsX,newsY+(spacing/2),"gameplay is what consumers care about most.",1,1
   If gamFashion=2 Then Text newsX,newsY+(spacing/2),"there is an appetite for more sophisticated games.",1,1
   If gamFashion=3 Then Text newsX,newsY+(spacing/2),"2D graphics are the most important aspect of a game.",1,1
   If gamFashion=4 Then Text newsX,newsY+(spacing/2),"3D graphics are the most important aspect of a game.",1,1
   If gamFashion=5 Then Text newsX,newsY+(spacing/2),"sound is the most important aspect of a game.",1,1
   If gamFashion=6 Then Text newsX,newsY+(spacing/2),"music is the most important aspect of a game.",1,1
  EndIf
  If eEvent(cyc)=201
   headline$="TAX CUTS"
   Text newsX,newsY-(spacing/2),"The government has lowered the",1,1
   Text newsX,newsY+(spacing/2),"tax rate to "+gamTax+"% for game developers.",1,1
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf  
  If eEvent(cyc)=202
   headline$="TAX HIKE"
   Text newsX,newsY-(spacing/2),"The government has increased the",1,1
   Text newsX,newsY+(spacing/2),"tax rate to "+gamTax+"% for game developers.",1,1
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=203
   headline$="REDUCED RENT"
   Text newsX,newsY-(spacing/2),"The rent for each workstation in your",1,1
   Text newsX,newsY+(spacing/2),"building has been reduced to $"+GetFigure$(gamRent)+".",1,1
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf  
  If eEvent(cyc)=204
   headline$="RAISED RENT"
   Text newsX,newsY-(spacing/2),"The rent for each workstation in your",1,1
   Text newsX,newsY+(spacing/2),"building has risen to $"+GetFigure$(gamRent)+".",1,1
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf 
  If eEvent(cyc)=205
   headline$="CHEAP FIX"
   Text newsX,newsY-(spacing/2),"The cost of repairing a faulty",1,1
   Text newsX,newsY+(spacing/2),"computer has fallen to $"+GetFigure$(gamRepairCost)+".",1,1
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf  
  If eEvent(cyc)=206
   headline$="COSTLY REPAIRS"
   Text newsX,newsY-(spacing/2),"The cost of repairing a faulty",1,1
   Text newsX,newsY+(spacing/2),"computer has risen to $"+GetFigure$(gamRepairCost)+".",1,1
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=207
   headline$="CHRISTMAS SHOPPING"
   Text newsX,newsY-(spacing/2),"As Christmas approaches, stores are anticipating",1,1
   Text newsX,newsY+(spacing/2),"a significant increase in sales this "+textPeriod$(optTime)+"!",1,1
   If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf 
  ;project issues
  If eEvent(cyc)=300
   headline$="END OF THE LINE" : v=eSubject(cyc)
   Text newsX,newsY-PercentOf#(spacing,80),"After "+GetFigure$(projSales(v))+" copies sold, '"+projTitle$(v)+"' has run",1,1
   Text newsX,newsY                       ,"its course and must now be withdrawn from sale.",1,1
   Text newsX,newsY+PercentOf#(spacing,80),"The remaining units have been buried in the desert.",1,1
  EndIf
  If eEvent(cyc)=301
   headline$="GLASS CEILING" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),projTitle$(v)+"'s potential sales are being limited",1,1
   Text newsX,newsY+(spacing/2),"by the "+hardTitle$(projFormat(v))+"'s audience of "+GetFigure$(hardUsers(projFormat(v)))+".",1,1
  EndIf
  If eEvent(cyc)=302
   headline$="PIRATES STRIKE" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"Pirated versions of '"+projTitle$(v)+"' are in circulation!",1,1
   Text newsX,newsY+(spacing/2),"Sales of the game are expected to halve.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=303
   headline$="CONTROVERSY CREATES CASH" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),"The controversial content of '"+projTitle$(v)+"'",1,1
   Text newsX,newsY+(spacing/2),"has generated an enormous amount of interest!",1,1
   If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=304
   headline$="TECHNICAL DIFFICULTIES"
   Text newsX,newsY-(spacing/2),charName$(char)+"'s workstation has broken! "+He$(g)+" won't",1,1
   Text newsX,newsY+(spacing/2),"be able to do any work until it is repaired.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=305
   headline$="HOT SEAT"
   Text newsX,newsY-(spacing/2),charName$(char)+" has moved to the "+DescribeNumber$(eRef(cyc))+" desk",1,1
   Text newsX,newsY+(spacing/2),"until "+Lower$(His$(g))+" own workstation is fixed.",1,1
  EndIf
  If eEvent(cyc)=306
   headline$="TIPPED FOR SUCCESS" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),fedName$(charFed(gamChar))+" have invested in a media campaign",1,1
   Text newsX,newsY+(spacing/2),"to promote the release of '"+projTitle$(gamRelease)+"'!",1,1
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=307
   headline$="ABANDONED OFFSPRING"
   Text newsX,newsY-(spacing/2),fedName$(charFed(gamChar))+" have distanced themselves from",1,1
   Text newsX,newsY+(spacing/2),"'"+projTitle$(gamRelease)+"' and are neglecting to promote it.",1,1
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=308
   headline$="GOOD AMBASSADOR" : v=eSubject(cyc)
   Text newsX,newsY-(spacing/2),charName$(char)+" has raised the profile of '"+projTitle$(gamRelease)+"'",1,1
   Text newsX,newsY+(spacing/2),"by discussing "+Lower$(His$(g))+" work on it in a recent interview!",1,1
   ;If eReaction(cyc)=0 Then PlaySound sCheer : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=309
   headline$="BAD INFLUENCE"
   Text newsX,newsY-(spacing/2),charName$(char)+" has damaged the prospects of '"+projTitle$(gamRelease)+"'",1,1
   Text newsX,newsY+(spacing/2),"by criticizing the project in a recent interview.",1,1
   ;If eReaction(cyc)=0 Then PlaySound sGrumble : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=310
   headline$="EMPTY DESK"
   Text newsX,newsY-(spacing/2),"You're paying rent on the "+DescribeNumber$(eRef(cyc))+" workstation when",1,1
   Text newsX,newsY+(spacing/2),"nobody is using it! You can close it at any time.",1,1
  EndIf
  If eEvent(cyc)=>311 And eEvent(cyc)=<316
   headline$="TECHNICAL DIFFICULTIES"
   Text newsX,newsY-(spacing/2),charName$(eRef(cyc))+"'s computer has crashed! The work "+Lower$(He$(g))+" did",1,1
   Text newsX,newsY+(spacing/2),"on the "+textAttribute$(eEvent(cyc)-310)+" of '"+projTitle$(eSubject(cyc))+"' has been lost.",1,1
   If eReaction(cyc)=0 Then PlaySound sFail : eReaction(cyc)=1
   refChar=eRef(cyc)
  EndIf
  If eEvent(cyc)=317 
   headline$="QUICK FIX"
   If charRoster(gamChar,eRef(cyc))>0
    Text newsX,newsY,charName$(char)+"'s workstation has now been repaired.",1,1
   Else
    Text newsX,newsY,"The "+DescribeNumber$(eRef(cyc))+" workstation has been successfully repaired.",1,1
   EndIf
   If eReaction(cyc)=0 Then PlaySound sProduce : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=318
   headline$="DO IT YOURSELF"
   Text newsX,newsY,charName$(char)+" has managed to fix "+Lower$(His$(g))+" computer by "+Him$(g)+"self!",1,1
   If eReaction(cyc)=0 Then PlaySound sProduce : eReaction(cyc)=1
  EndIf
  If eEvent(cyc)=319
   headline$="COMPANY POLICY"
   Text newsX,newsY-PercentOf#(spacing,80),"In accordance with their policy of only making games",1,1
   Text newsX,newsY,"for "+fedBrand$(charFed(gamChar))+" products, "+fedName$(charFed(gamChar))+" have insisted that",1,1
   Text newsX,newsY+PercentOf#(spacing,80),"you use the '"+hardTitle$(gamHardware)+"' for your latest project.",1,1
  EndIf
  ;mission reminders
  If eEvent(cyc)=401
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's gameplay to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=402
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's programming to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=403
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's 2D graphics to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=404
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's 3D graphics to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=405
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's sound to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=406
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to improve the latest",1,1
   Text newsX,newsY+(spacing/2),"project's music to "+gamTarget+"% by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=407
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to increase the size",1,1
   Text newsX,newsY+(spacing/2),"of your team to "+gamTarget+" people by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=409
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to acquire a",1,1
   Text newsX,newsY+(spacing/2),"bank balance of $"+GetFigure$(gamTarget)+" by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=410
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you've been given the deadline",1,1
   Text newsX,newsY+(spacing/2),"of getting out of debt by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=411
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you've been given the deadline of",1,1
   Text newsX,newsY+(spacing/2),"signing with a major company by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=412
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to recruit a",1,1
   Text newsX,newsY+(spacing/2),"developer called '"+charName$(gamTarget)+"' by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  If eEvent(cyc)=413
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you're on a mission to remove",1,1
   Text newsX,newsY+(spacing/2),"'"+charName$(gamTarget)+"' from your team as soon as possible!",1,1
  EndIf
  If eEvent(cyc)=414
   headline$="MISSION IMPOSSIBLE"
   Text newsX,newsY-(spacing/2),"Remember you've been given the deadline of",1,1
   Text newsX,newsY+(spacing/2),"releasing '"+projTitle$(gamTarget)+"' by "+DescribeDate$(gamDeadline,0)+"!",1,1
  EndIf
  ;display headline
  Color 0,0,0
  SqueezeNewsFont(headline$,670,70)
  Text newsX,y-80,headline$,1,1
  ;photo display
  photoX=x-140 : photoY=y+60
  If eStats(cyc)=0
   DrawImage gNewsScene(eScene(cyc,1)),photoX-10,photoY
   DrawImage gNewsScene(eScene(cyc,2)),photoX+310,photoY
  EndIf
  If eStats(cyc)=1
   DrawImage gNewsScene(3),photoX,photoY
   If projIdentified(eSubject(cyc))>0
    DrawImage projBox(eSubject(cyc)),photoX,photoY
   Else
    DrawImage projScreen(eSubject(cyc)),photoX,photoY
   EndIf
   If refChar>0 Then DrawImage charPhoto(refChar),photoX+120,photoY Else DrawImage charPhoto(gamChar),photoX+120,photoY
   DrawProjectProfile(eSubject(cyc),x-15,photoY-86,0)
  EndIf
  If eStats(cyc)=2
   DrawImage gNewsScene(3),photoX,photoY
   DrawImage gHardware(eSubject(cyc)),photoX,photoY
   ;For f=1 To no_feds
    ;If fedName$(f)=hardBrand$(eSubject(cyc)) Then DrawImage gLabel(f),photoX,photoY-65
   ;Next
   DrawHardwareProfile(eSubject(cyc),photoX+250,photoY-86,0)
  EndIf
  If eStats(cyc)=3 And char>0
   DrawImage gNewsScene(1),photoX,photoY
   If charFed(char)>0
    DrawImage gLabel(charFed(char)),photoX,photoY
   Else
    DrawImage gLabel(eRef(cyc)),photoX,photoY
   EndIf
   If refChar>0 Then DrawImage charPhoto(refChar),photoX-120,photoY
   DrawImage charPhoto(char),photoX+120,photoY+5
   DrawCharProfile(char,photoX+260,photoY-86,0)
  EndIf
  If eStats(cyc)=4
   DrawImage gNewsScene(1),photoX-10,photoY
   DrawImage gLabel(eSubject(cyc)),photoX-10,photoY
   DrawImage charPhoto(fedBooker(eSubject(cyc))),photoX+110,photoY
   DrawImage gNewsScene(2),photoX+310,photoY
  EndIf
  ;prompt
  If gotim>20 And eUpdated(cyc)>0
   SetFont fontNews(2)
   Outline(">>> CLICK TO PROCEED >>>",newsX,GraphicsHeight()-25,100,100,100,255,255,255)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
;PlaySound sMenuGo 
screen=28
End Function

;/////////////////////////////////////////////////////////////////////////////////////
;------------------------------ RELATED FUNCTIONS ------------------------------------
;/////////////////////////////////////////////////////////////////////////////////////

;DISPLAY MAGAZINE
Function DisplayMagazine(cyc,x,y)
  ;base image
  DrawImage gMagazine,x,y
  DrawImage gNewsIdentity(eNewspaper(cyc)),x-88,y-205
  ;adverts
  adX=x+260 : adY=y-204
  DrawImage gNewsAdvert(eAdvert(cyc)),adX,adY
  If eAdvert(cyc)=10
   SetFont fontNews(10)
   Outline(gamTax+"%",adX-25,adY,0,0,0,255,0,0)
  EndIf
  If eAdvert(cyc)=11
   SetFont fontNews(5)
   OutlineStraight("$"+GetFigure$(gamRent),adX,adY+35,0,0,0,255,240,0)
  EndIf
  If eAdvert(cyc)=12
   SetFont fontNews(3)
   Outline("Current fashion:",adX,adY-37,0,0,0,225,175,225)
   DrawImage gIconSmall(gamFashion),adX-40,adY-1 : DrawImage gIconSmall(gamFashion),adX+40,adY-1
   DrawImage gIconLarge(gamFashion),adX,adY-1
   SetFont fontNews(4)
   Outline(textFashion$(gamFashion),adX,adY+33,0,0,0,100,200,100)
  EndIf
  If eAdvert(cyc)=13
   If negFormat=-1
    its=0
    Repeat
     negFormat=Rnd(0,5) : its=its+1
    Until formatActive(negFormat) Or its>100
   EndIf
   DrawImage gFormat(negFormat),adX-30,adY+2
   SetFont fontNews(7)
   Outline("$"+formatCost(negFormat),adX+40,adY+2,0,0,0,100,200,100)
   SetFont fontNews(1)
   If negFormat=0 Then namer$="for data" Else namer$="per "+Lower$(formatName$(negFormat))
   Outline(namer$,adX,adY+37,0,0,0,255,255,255)
  EndIf
  If eAdvert(cyc)=14
   SetFont fontNews(3)
   OutlineStraight("REPAIRS:",adX-70,adY-36,0,0,0,255,200,175)
   SetFont fontNews(4)
   OutlineStraight("$"+GetFigure$(gamRepairCost),adX+5,adY+36,0,0,0,255,240,0)
  EndIf
  ;smallprint
  Color 150,150,150
  Line x-315,y-126,x+330,y-126
  If screen=23 Or screen=25 Then Line x-315,y-36,x+330,y-36
  SetFont fontNews(1) : Color 100,100,100
  namer$="Interactive Entertainment News"
  If screen=28 Then namer$="Interactive Entertainment Charts"
  If screen=29 Then namer$="Interactive Entertainment Reviews"
  Text x-295,y-143,namer$,0,1
  If screen=29 And projReleased(gamProject)>0 Then namer$=DescribeDate$(projReleased(gamProject),1) Else namer$=DescribeDate$(gamDate,1)
  Text x+70,y-143,namer$,1,1
  Text x+320,y-143,"$3.50",1,1
End Function

;RESET OLD VALUES
Function ResetOldValues(char)
 charOldDesign(char)=charDesign(char)
 charOldProgram(char)=charProgram(char)
 charOld2D(char)=char2D(char)
 charOld3D(char)=char3D(char)
 charOldSound(char)=charSound(char)
 charOldMusic(char)=charMusic(char)
 charOldRep(char)=charRep(char)
End Function

;RESET NEW VALUES
Function ResetNewValues(char)
 charNewDesign(char)=charDesign(char)
 charNewProgram(char)=charProgram(char)
 charNew2D(char)=char2D(char)
 charNew3D(char)=char3D(char)
 charNewSound(char)=charSound(char)
 charNewMusic(char)=charMusic(char)
 charNewRep(char)=charRep(char)
End Function

;PURSUE NEW STATISTIC
Function PursueValue(current,target,limit)
 value=0
 ;climb level 1
 If current<target Then value=1
 If current>target Then value=-1
 ;climb level 2
 If current=<target-10 Then value=Rnd(2,4)
 If current=>target+10 Then value=-Rnd(2,4)
 ;climb level 3
 If current=<target-20 Then value=Rnd(3,6)
 If current=>target+20 Then value=-Rnd(3,6)
 ;climb level 4
 If current=<target-30 Then value=Rnd(4,8)
 If current=>target+30 Then value=-Rnd(4,8)
 ;climb level 5
 If current=<target-40 Then value=Rnd(5,10)
 If current=>target+40 Then value=-Rnd(5,10)
 ;double up
 If limit=200 Then value=value*2
 ;limit changes
 If limit>0 And limit<200
  If value>limit Then value=limit
  If value<-limit Then value=-limit
 EndIf
 Return value
End Function

;CHECK ALTERED VALUES
Function CheckNewValues(char)
 If charNewDesign(char)<25 Then charNewDesign(char)=25
 If charNewDesign(char)>99 Then charNewDesign(char)=99
 If charNewProgram(char)<25 Then charNewProgram(char)=25
 If charNewProgram(char)>99 Then charNewProgram(char)=99
 If charNew2D(char)<25 Then charNew2D(char)=25
 If charNew2D(char)>99 Then charNew2D(char)=99
 If charNew3D(char)<25 Then charNew3D(char)=25
 If charNew3D(char)>99 Then charNew3D(char)=99 
 If charNewSound(char)<25 Then charNewSound(char)=25
 If charNewSound(char)>99 Then charNewSound(char)=99
 If charNewMusic(char)<25 Then charNewMusic(char)=25
 If charNewMusic(char)>99 Then charNewMusic(char)=99 
 If charNewRep(char)<25 Then charNewRep(char)=25
 If charNewRep(char)>99 Then charNewRep(char)=99 
End Function

;QUEUE REACTIONS TO TRADE
Function FindTradeReactions(char,label)
 For v=1 To no_chars
  If char<>v And charTeam(char)<>charTeam(v)
   ;react to departures
   If charFed(char)=charFed(v) And label<>charFed(v)
    If charRelationship(v,char)>0 Then charTradeReaction(v)=-char
    If charRelationship(v,char)<0 Then charTradeReaction(v)=char
   EndIf
   ;react to arrivals
   If charFed(char)<>charFed(v) And label=charFed(v)
    If charRelationship(v,char)>0 Then charTradeReaction(v)=char
    If charRelationship(v,char)<0 Then charTradeReaction(v)=-char
   EndIf
  EndIf
 Next
End Function