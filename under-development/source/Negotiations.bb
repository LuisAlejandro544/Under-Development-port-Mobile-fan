;//////////////////////////////////////////////////////////////////////////////
;------------------------- UNDER DEVELOPMENT: NEGOTIATIONS --------------------
;//////////////////////////////////////////////////////////////////////////////

;----------------------------------------------------------------
;//////////////////// RISK CONTRACT OFFERS //////////////////////
;----------------------------------------------------------------
Function RiskContractOffers()
 ;renewal
 fed=0 
 If charContract(gamChar)=0 And charFed(gamChar)<no_feds Then fed=charFed(gamChar)
 ;new offer
 If fed=0 And CountExperience(gamChar,0)>4 And CountReleases()>0
  chance=(100-GetValue(gamChar))/2
  If charContract(gamChar)>0 And chance<charContract(gamChar)*10 Then chance=charContract(gamChar)*10
  If chance<10 Then chance=10
  randy=Rnd(0,chance)
  interest=2
  If gamRelease>0 Then interest=interest+1
  If gamRelease>0 And chartRank(1)=>1 And chartRank(1)=<10 And charFed(gamChar)=no_feds Then interest=interest+1
  If chartChar(chartList(1))=gamChar Then interest=interest+1
  If randy=<interest
   its=0
   Repeat
    fed=Rnd(1,no_feds-1) : satisfied=1
    randy=Rnd(0,1)
    If randy=0 And charRelationship(fedBooker(fed),gamChar)<0 Then satisfied=0
    randy=Rnd(0,1)
    If randy=0 And charExperience(gamChar,fed)>0 Then satisfied=0
    randy=Rnd(0,1)
    If (randy=0 And fedSize(fed)>20) Or fedSize(fed)=>optRosterLim Then satisfied=0
    If fed=charFed(gamChar) Then satisfied=0 
    ;If fedBank(fed)/2<charWorth(gamChar)*charContract(gamChar) Then satisfied=0 
    its=its+1
    If its>100 Then fed=0 : satisfied=1
   Until satisfied=1
  EndIf
 EndIf
 ;execute
 If fed>0 And fedBooker(fed)>0 Then negChar=fedBooker(fed) : screen=52 : screenAgenda=0 : NegotiateDeal()
End Function

;------------------------------------------------------------------------------
;///////////////////////// 52. DEAL NEGOTIATIONS //////////////////////////////
;------------------------------------------------------------------------------
Function NegotiateDeal()
;get setting
Loader("Please Wait","Meeting "+charName$(negChar))
;chThemeVol#=0.5 : ChannelVolume chTheme,chThemeVol#
fed=charFed(negChar)
negSetting=1 : camFoc=2
PrepareMeeting()
;get working value
negWorth=CalculateWorth(gamChar)
negChunk=GetValue(gamChar)/3
If charFed(gamChar)=no_feds Then PercentOf#(negChunk,75) ;unemployed
If negChunk<5 Then negChunk=5
If fedPolicy(fed)=2 Then negWorth=negWorth*2 : negChunk=0
If fedPolicy(fed)=3 Then negWorth=0 : negChunk=PercentOf#(negChunk,150)
;learning curve
;curve=25+(CountExperience(gamChar,0)/2)
;If curve>100 Then curve=100
;negChunk=Int(PercentOf#(negChunk,curve))
;false perceptions 
negWorth=PercentOf#(negWorth,Rnd(75,125))
negChunk=Int(PercentOf#(negChunk,Rnd(90,110)))
;initial contract
negChances=Rnd(1,6)
negContract=24/optTime
negContract=Rnd(negContract/2,negContract*2)
If fedPolicy(fed)=4 Then negContract=negContract*2
If negContract>48 Then negContract=48
If fedPolicy(fed)=5 Then negContract=negContract/2
negTeamMultiple=2
negPayOff=(negWorth*negTeamMultiple)*negContract
negPayOff=PercentOf#(negPayOff,Rnd(75,100))
negPayOff=RoundOff(negPayOff,100)
If negPayOff>10000 Then negPayOff=RoundOff(negPayOff,1000)
negRoyalties=PercentOf#(negChunk,Rnd(80,100))
negInitialPayOff=negPayOff
negInitialSalary=negSalary
negInitialRoyalties=negRoyalties
negInitialContract=negContract
;reset progress
negTim=0 : negStage=0
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
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And (negStage=1 Or negStage=3)
	 ;adjust contract
	 If negStage=1 And foc=>1 And foc=<5
	  ;browse left
	  If MouseDown(2)
	   range=FigureRange(negPayOff)
	   If range<1000 Then range=1000
	   If foc=1 Then negPayOff=negPayOff-range : PlaySound sMenuBrowse : keytim=3
       If foc=2 Then negRoyalties=negRoyalties-1 : PlaySound sMenuBrowse : keytim=3
	   If foc=3 Then negContract=negContract-1 : PlaySound sMenuBrowse : keytim=3
	  EndIf
	  ;browse right
	  If MouseDown(1)
	   range=FigureRange(negPayOff)
	   If range<1000 Then range=1000
	   If foc=1 Then negPayOff=negPayOff+range : PlaySound sMenuBrowse : keytim=3
	   If foc=2 Then negRoyalties=negRoyalties+1 : PlaySound sMenuBrowse : keytim=3 
	   If foc=3 Then negContract=negContract+1 : PlaySound sMenuBrowse : keytim=3
	  EndIf
	  ;reset
	  If KeyDown(14)
	   PlaySound sMenuBrowse : keytim=10
	   If foc=1 Then negPayOff=negInitialPayOff
	   If foc=2 Then negRoyalties=negInitialRoyalties
	   If foc=3 Then negContract=negInitialContract
	  EndIf
	 EndIf
	 ;submit proposal
	 If foc=5 And MouseDown(1)
	  PlaySound sMenuGo : keytim=15
	  If negStage=1 
	   negStage=2 : negTim=0
	   If DealChanged() Then negVerdict=GetDealVerdict() Else negVerdict=0
	  EndIf
	  If negStage=3 Or DealChanged()=0 Then negStage=4 : negTim=0
	 EndIf
	 ;cancel
	 If (foc=4 And MouseDown(1)) Or KeyDown(1)
	  PlaySound sMenuBack : keytim=15
	  negStage=5 : negTim=0
	 EndIf
	EndIf
	;check limits
	If negPayOff<0 Then negPayOff=0
	If negPayOff>1000000 Then negPayOff=1000000  
    If negRoyalties<0 Then negRoyalties=0
    If negRoyalties>100 Then negRoyalties=100 
	If negContract<1 Then negContract=1
	If negContract>48 Then negContract=48
	
	;PLAYER CYCLE
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
  If viable=1 And pFoc(cyc)>0 And pChar(cyc)>0 And pChar(pFoc(cyc))>0
   PointHead(cyc,FindChild(p(pFoc(cyc)),"Head"))
  EndIf
 Next
 
 RenderWorld 1

 ;DISPLAY
 ;reset speech
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------- INTRODUCTIONS -----------------
 ;renew with existing label
 If negStage=0 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,1,2)
   lineA$="Hi, "+charName$(gamChar)+". Your contract with us has"
   lineB$="expired, so we need to talk about your future..."
  EndIf
  If negTim>350 And negTim<650 And negWorth<charWorth(gamChar)
   Speak(2,1,1)
   lineA$="To be honest, things aren't working out too well"
   lineB$="and we'd like to reflect that with these changes:"
  EndIf
  If negTim>350 And negTim<650 And negWorth=>charWorth(gamChar)
   Speak(2,1,3)
   lineA$="I must admit that we're pleased with your progress,"
   lineB$="so we'd be happy to keep you on with this deal:"
  EndIf
  If negTim>650 Then camFoc=1
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;approached by new label
 If negStage=0 And fed<>charFed(gamChar)
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)=0
   Speak(2,1,3)
   lineA$="Nice to meet you, "+charName$(gamChar)+". We invited you"
   lineB$="here today to discuss working for "+fedName$(fed)+"?"
  EndIf
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)>0
   Speak(2,1,3)
   lineA$="Nice to see you again, "+charName$(gamChar)+". We'd like to"
   lineB$="talk about bringing you back to "+fedName$(fed)+"?"
  EndIf
  If negTim>350 And negTim<650
   If fedPolicy(fed)>0
    Speak(2,1,2)
    lineA$="Before we go any further, you should know that we have a"
    If fedPolicy(fed)=1 Then lineB$="policy of only making games for "+fedBrand$(fed)+" products."
    If fedPolicy(fed)>1 Then lineB$="policy of "+textPolicy$(fedPolicy(fed))+"."
   Else
    negTim=675
   EndIf
  EndIf
  If negTim>675 And negTim<975 And negWorth=<charWorth(gamChar)
   Speak(2,1,1)
   lineA$="You're not exactly a priority for us right now, but"
   lineB$="we'd consider bringing you onboard with this deal:"
  EndIf
  If negTim>675 And negTim<975 And negWorth>charWorth(gamChar)
   Speak(2,1,3)
   lineA$="We feel you'd make a great addition to the roster,"
   lineB$="and we're prepared to make it happen with this deal:"
  EndIf
  If negTim>975 Then camFoc=1
  If negTim>1000 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;--------------- CONTRACT SETUP --------------------
 foc=0
 If negStage=1 Or negStage=3
  ;facial expression
  mood=2
  If foc=4 Then mood=1
  If foc=5 Then mood=3
  Speak(1,2,mood)
  ;options
  y=455
  Color 0,0,0 : Rect rX#(0),rY#(y),rX#(800),rY#(600-y),1
  DrawOption(4,rX#(125),rY#(y+72),"<<< WITHDRAW <<<","")
  DrawOption(1,rX#(400),rY#(y+32),"Advance","$"+GetFigure$(negPayOff))
  DrawOption(2,rX#(400),rY#(y+72),"Royalties",negRoyalties+"%")
  DrawOption(3,rX#(400),rY#(y+112),"Contract",negContract+" "+textPeriod$(optTime)+"s") 
  namer$=">>> PROPOSE >>>"
  If DealChanged()=0 Then namer$=">>> ACCEPT >>>"
  If negStage=3 Then namer$=">>> CONFIRM >>>"
  DrawOption(5,rX#(675),rY#(y+72),namer$,"")
  ;profile reminder
  ;If (foc=0 Or foc=>4) And MouseDown(2) Then DrawCharProfile(gamChar,rX#(400),rY#(10),1)
  DrawCharProfile(gamChar,rX#(400),rY#(5),1)
 EndIf
 ;------------------ VERDICTS -----------------------------
 If negStage=2 And negTim>10 Then camFoc=2
 ;acceptances
 If negStage=2 And negVerdict=0
  If negTim>25 And negTim<325
   Speak(2,1,3)
   greenLineA$="OK, I'm happy to draw up that contract for you!"
   greenLineB$="Just confirm the details and it's a done deal..."
  EndIf
  If negTim>350 Then negStage=3 : negTim=0 : keytim=15
 EndIf
 ;payment objections
 If negStage=2 And negVerdict=10
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, everything about that contract is wrong!"
   redLineB$="At least one of those figures has to come down..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=11
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you can't substantiate a $"+GetFigure$(negPayOff)+" advance!"
   redLineB$="The figure we offered you was perfectly reasonable..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=12
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you need to stop focusing on cash upfront!"
   redLineB$="Would you be prepared to accept more royalties?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=13
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you're in no position to demand "+negRoyalties+"% royalties!"
   redLineB$="The points we offered you were perfectly reasonable..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=14
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you need to stop focusing on royalties!"
   redLineB$="Wouldn't you prefer more cash upfront?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=15
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you don't deserve a $"+GetFigure$(negPayOff)+" advance!"
   redLineB$="You're obviously not taking this seriously..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=16
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, you can't have "+negRoyalties+"% of the royalties!"
   redLineB$="You obviously don't understand this business..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=17
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="What's the point in signing up for that amount of"
   redLineB$="time? We won't be able to accomplish anything!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=18
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, a "+negContract+"-"+textPeriod$(optTime)+" contract isn't worth that much!"
   redLineB$="We can't invest in you if you're not committed..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=19
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="No, if you're not willing to sign up for "+negInitialContract+" "+textPeriod$(optTime)+"s"
   redLineB$="then you can no longer command a $"+GetFigure$(negPayOff)+" advance..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=20
  If negTim>25 And negTim<325
   Speak(2,1,1) 
   redLineA$="You don't have enough money in the bank to survive"
   redLineB$="without an advance! You need to be responsible..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=21
  If negTim>25 And negTim<325
   Speak(2,1,2) 
   redLineA$="Sorry, we have a policy of not paying royalties."
   redLineB$="You'll have to accept a generous advance instead..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=22
  If negTim>25 And negTim<325
   Speak(2,1,2) 
   redLineA$="Sorry, we have a policy of not paying advances."
   redLineB$="You'll have to accept better royalties instead..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=23
  If negTim>25 And negTim<325
   Speak(2,1,2) 
   redLineA$="Sorry, we have a policy of signing employees to"
   redLineB$="long-term contracts. It has to be at least "+negInitialContract+" "+textPeriod$(optTime)+"s..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf   
 If negStage=2 And negVerdict=24
  If negTim>25 And negTim<325
   Speak(2,1,2) 
   redLineA$="Sorry, we have a policy of signing employees to"
   redLineB$="short-term contracts. It can't exceed "+negInitialContract+" "+textPeriod$(optTime)+"s..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf    
 ;----------------- ENDINGS -----------------------
 If negStage=>4 And negTim>10 Then camFoc=2
 ;successful deal
 If negStage=4 And fed<>charFed(gamChar)
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)=0
   Speak(2,1,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : gamBank=gamBank+negPayOff : PlaySound sCash : charged=1
   lineA$="Great. Welcome to "+fedName$(fed)+", "+charName$(gamChar)+"!"
   lineB$="I'm sure you'll be an asset to the company..."
  EndIf
  If negTim>25 And negTim<325 And charExperience(gamChar,fed)>0
   Speak(2,1,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : gamBank=gamBank+negPayOff : PlaySound sCash : charged=1
   lineA$="Great, welcome back to "+fedName$(fed)+"!"
   lineB$="We look forward to seeing your new stuff..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;successful renewl
 If negStage=4 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,1,3)
   If charged=0 Then fedBank(fed)=fedBank(fed)-negPayOff : gamBank=gamBank+negPayOff : PlaySound sCash : charged=1
   lineA$="Good, I'm glad we could come to an arrangement!"
   lineB$="Let's make the next "+negContract+" "+textPeriod$(optTime)+"s the best yet..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;deal breakdown
 If negStage=5 And fed<>charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,1,1)
   redLineA$="Alright, this meeting isn't going anywhere."
   redLineB$="Let's just forget about it for the time being..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;renewal breakdown
 If negStage=5 And fed=charFed(gamChar)
  If negTim>25 And negTim<325
   Speak(2,1,2)
   redLineA$="Well, we're obviously not on the same page anymore"
   redLineB$="so we wish you luck finding employment elsewhere..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
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
;update status
If negStage=4
 If charFed(gamChar)<>fed
  MoveTeam(gamChar,fed)
  charRep(gamChar)=charRep(gamChar)+1
  If charContract(gamChar)>1 Then ChangeRelationship(gamChar,fedBooker(charFed(gamChar)),-1)
  ;If fedPolicy(fed)=1 And hardBrand$(gamHardware)<>fedBrand$(fed) And hardBrand$(gamHardware)<>fedName$(fed)
   ;For v=1 To no_machines
    ;If hardReleased(v)>0 And (hardBrand$(v)=fedBrand$(fed) Or hardBrand$(v)=fedName$(fed)) 
     ;gamHardware=v
    ;EndIf
   ;Next
   ;hardOwned(gamHardware)=1
  ;EndIf
 EndIf
 charWorth(gamChar)=negWorth
 charRoyalties(gamChar)=negRoyalties
 charContract(gamChar)=negContract
 If charHappiness(gamChar)<0 Then charHappiness(gamChar)=0
 charHappiness(gamChar)=charHappiness(gamChar)+1
Else
 If fed=charFed(gamChar)
  MoveTeam(gamChar,no_feds)
  GenerateContract(gamChar)
  charRep(gamChar)=charRep(gamChar)-1
  charHappiness(gamChar)=charHappiness(gamChar)-1
 EndIf
EndIf
;proceed
screen=20
End Function

;----------------------------------------------------------------
;////////////////// RISK COLLABORATION OFFERS ///////////////////
;----------------------------------------------------------------
Function RiskCollaborations()
 ;collaboration offers
 negTopic=2 : negChar=0 : fed=charFed(gamChar)
 chance=(120-charRep(gamChar))
 If charFed(gamChar)<no_feds Then chance=chance/2
 randy=Rnd(0,chance)
 If randy=<1 And TeamSize(gamChar)<optTeamSize And fedSize(fed)<optRosterLim And CountReleases()>0
  its=0
  Repeat
   satisfied=1 : its=its+1
   negChar=Rnd(1,no_chars)
   randy=Rnd(0,2)
   If randy=0 And charRep(negChar)>charRep(gamChar) Then satisfied=0
   If randy=1 And charFed(gamChar)<>charFed(negChar) And charContract(negChar)>1 Then satisfied=0
   If charRelationship(negChar,gamChar)<0 Then satisfied=0
   If charFed(gamChar)=no_feds And charFed(negChar)<no_feds Then satisfied=0
   If charTeam(negChar)=gamChar Or charFed(negChar)=0 Then satisfied=0 
   If its>1000 Then negChar=0 : satisfied=1
  Until satisfied=1
 EndIf
 If negTopic>0 And negChar>0 And screen<>25 And screen<>29 Then negSetting=1 : screen=54 : screenAgenda=0 : RecruitMember()
 ;other offers
 negSetting=0
 If (gamMission=>1 And gamMission=<6) Or gamMission=14 Then mission=1 Else mission=0
 If negChar=0
  negTopic=0 
  Repeat
   negChar=Rnd(1,no_chars)
  Until charTeam(negChar)<>gamChar And charFed(negChar)>0
  randy=Rnd(0,25)
  If charRelationship(gamChar,negChar)=>0
   If randy=0 And charRelationship(gamChar,negChar)=0 And charExperience(negChar,0)=0 Then negTopic=30 ;socialize
   If randy=1 And gamBank>0 Then negTopic=10 ;expert training
   If randy=2 And projReleased(gamProject)=0 And gamBank>0 Then negTopic=14 ;buy aspect of work
   If randy=3 And gamBank>0 And mission=0 Then negTopic=15 ;buy half-baked project
   If randy=4 And AverageProjectStats(gamProject,1)>AverageHardwareStats(projFormat(gamProject))/2 And projReleased(gamProject)=0
    negTopic=16 ;sell aspect of work
   EndIf
   If randy=5 And CountReleases()>0 Then negTopic=17 ;sell rights to project
  EndIf 
  randy=Rnd(0,100/TeamSize(gamChar))
  If randy=<1 And TeamSize(gamChar)>1 Then negSetting=1 : negTopic=20 ;sell employee
  If negTopic>0 And screen<>25 And screen<>29 And screen<>50 Then screen=53 : screenAgenda=0 : Meeting() 
 EndIf
End Function

;------------------------------------------------------------------------------
;/////////////////////////// 57. RECRUIT MEMBER ///////////////////////////////
;------------------------------------------------------------------------------
Function RecruitMember()
;get setting
Loader("Please Wait","Meeting "+charName$(negChar))
;chThemeVol#=0.5 : ChannelVolume chTheme,chThemeVol#
If negTopic=2 Then fed=charFed(gamChar) Else fed=charFed(negChar)
negSetting=1 : camFoc=2
PrepareMeeting()
;get working value
negWorth=CalculateWorth(negChar)
negWorth=PercentOf#(negWorth,Rnd(75,125))
;initial contract
negChances=Rnd(1,6)
negPayOff=0 : negBuyOut=0
If charContract(negChar)>0 And charFed(negChar)<>charFed(gamChar) ;And fedPolicy(charFed(gamChar))<>7
 negBuyOut=charContract(negChar)*charSalary(negChar)
 negPayOff=negBuyOut
EndIf
If charTeam(negChar)=gamChar Then negSalary=charSalary(negChar) Else negSalary=0
negContract=24/optTime
negPayOffPrefer=Rnd(-10,4)
If negPayOffPrefer<0 Then negPayOffPrefer=0
;collaboration demands
If negTopic=2
 negContract=Rnd(negContract/2,negContract)
 negPayOffPrefer=Rnd(-8,4)
 If negPayOffPrefer<0 Then negPayOffPrefer=0
 If negPayOffPrefer>0
  negPayOff=negWorth*negContract
  negPayOff=PercentOf#(negPayOff,negPayOffPrefer*25)
  negPayOff=PercentOf#(negPayOff,Rnd(50,100))
  negSalary=PercentOf#(negWorth*negContract,25*(4-negPayOffPrefer))/negContract
  negSalary=PercentOf#(negSalary,Rnd(50,100))
 Else
  negSalary=PercentOf#(negWorth,Rnd(50,100))
  negPayOff=0
 EndIf
 negBuyOut=0
EndIf 
;clock initial values
negPayOff=RoundOff(negPayOff,100)
If negPayOff>10000 Then negPayOff=RoundOff(negPayOff,1000)
negSalary=RoundOff(negSalary,100)
negInitialPayOff=negPayOff
negInitialSalary=negSalary
negInitialRoyalties=negRoyalties
negInitialContract=negContract
;reset progress
negTim=0 : negStage=0
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
	
	;CONFIGURATION
	If gotim>20 And keytim=0 And (negStage=1 Or negStage=3)
	 ;adjust contract
	 If negStage=1
	  ;browse left
	  If MouseDown(2)
	   range=FigureRange(negPayOff-negBuyOut)
	   If range<1000 Then range=1000
	   If foc=1 Then negPayOff=negPayOff-range : PlaySound sMenuBrowse : keytim=2
	   range=FigureRange(negSalary)
	   If range<100 Then range=100
	   If foc=2 Then negSalary=negSalary-range : PlaySound sMenuBrowse : keytim=2
	   If foc=3 Then negContract=negContract-1 : PlaySound sMenuBrowse : keytim=3
	  EndIf
	  ;browse right
	  If MouseDown(1)
	   range=FigureRange(negPayOff-negBuyOut)
	   If range<1000 Then range=1000
	   If foc=1 Then negPayOff=negPayOff+range : PlaySound sMenuBrowse : keytim=2
	   range=FigureRange(negSalary)
	   If range<100 Then range=100
	   If foc=2 Then negSalary=negSalary+range : PlaySound sMenuBrowse : keytim=2
	   If foc=3 Then negContract=negContract+1 : PlaySound sMenuBrowse : keytim=3
	  EndIf
	  ;reset
	  If KeyDown(14)
	   PlaySound sMenuBrowse : keytim=10
	   If foc=1 Then negPayOff=negInitialPayOff
	   If foc=2 Then negSalary=negInitialSalary
	   If foc=3 Then negContract=negInitialContract
	  EndIf
	 EndIf
	 ;submit proposal
	 If foc=4 And MouseDown(1)
	  PlaySound sMenuGo : keytim=15
	  If negStage=1 
	   negStage=2 : negTim=0
	   If negTopic=1 Or DealChanged() Then negVerdict=GetRecruitVerdict() Else negVerdict=0
	  EndIf
	  If negStage=3 Or (negTopic=2 And DealChanged()=0) Then negStage=4 : negTim=0
	 EndIf
	 ;cancel
	 If (foc=5 And MouseDown(1)) Or KeyDown(1)
	  PlaySound sMenuBack : keytim=15
	  negStage=5 : negTim=0
	 EndIf
	EndIf
	;check limits
	If negPayOff<0 Then negPayOff=0  
	;If negPayOff>gamBank Then negPayOff=gamBank
	If negPayOff>1000000 Then negPayOff=1000000 
	If negBuyOut>0 And negPayOff<negBuyOut Then negPayOff=negBuyOut
	If negSalary<0 Then negSalary=0 
	;If negSalary>gamBank Then negSalary=gamBank
	If negSalary>25000 Then negSalary=25000
	If negContract<1 Then negContract=1
	If negContract>48 Then negContract=48
	
	;PLAYER CYCLE
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
  If charFed(pChar(cyc))=6 And pChar(cyc)<>fedBooker(charFed(pChar(cyc))) Then viable=0
  If viable=1 And pFoc(cyc)>0 And pChar(cyc)>0 And pChar(pFoc(cyc))>0
   PointHead(cyc,FindChild(p(pFoc(cyc)),"Head"))
  EndIf
 Next
 
 RenderWorld 1

 ;DISPLAY
 ;reset speech
 For cyc=1 To no_plays
  pSpeaking(cyc)=0
 Next
 ;reset subtitles
 lineA$="" : lineB$=""
 redLineA$="" : redLineB$=""
 greenLineA$="" : greenLineB$=""
 ;------------- INTRODUCTIONS -----------------
 ;renew existing member
 If negStage=0 And negTopic=1 And charTeam(negChar)=gamChar
  If negTim>25 And negTim<325
   Speak(negStar,negVisitor,2)
   lineA$="Listen, "+charName$(negChar)+", your contract with me has"
   lineB$="expired so we need to talk about your future..."
  EndIf
  If negTim>350 And negTim<650 And negWorth<charWorth(negChar)
   Speak(negStar,negVisitor,1)
   lineA$="To be honest, things aren't working out too well"
   lineB$="and I'd like to reflect that with these changes:"
  EndIf
  If negTim>350 And negTim<650 And negWorth=>charWorth(negChar)
   Speak(negStar,negVisitor,3)
   lineA$="I must admit that I'm pleased with your progress,"
   lineB$="so I'd be happy to keep you on with this deal:"
  EndIf
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;recruit new member (internally)
 If negStage=0 And negTopic=1 And charTeam(negChar)<>gamChar And (charFed(negChar)=charFed(gamChar) Or charContract(negChar)=0)
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)=<0
   Speak(negStar,negVisitor,3)
   lineA$="Nice to meet you, "+charName$(negChar)+". I'd like to talk"
   lineB$="about bringing you onboard for my latest project?"
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)>0
   Speak(negStar,negVisitor,3)
   lineA$="Hi, "+charName$(negChar)+". Since we're friends, how would"
   lineB$="you feel about joining me on my latest project?"
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)>0
   Speak(negStar,negVisitor,3)
   lineA$="Nice to see you again, "+charName$(negChar)+". I'd like to talk"
   lineB$="about bringing you back onboard for my latest project?"
  EndIf
  If negTim>350 And negTim<650
   Speak(negStar,negVisitor,3)
   lineA$="I feel you'd make a great addition to the team,"
   lineB$="and I'm prepared to make it happen with this deal:"
  EndIf
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;recruit new member (externally)
 If negStage=0 And negTopic=1 And charTeam(negChar)<>gamChar And charFed(negChar)<>charFed(gamChar) And charContract(negChar)>0
  If charFed(gamChar)=no_feds Then namer$="my bedroom" Else namer$=fedName$(charFed(gamChar))
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)=<0
   Speak(negStar,negVisitor,3)
   lineA$="Nice to meet you, "+charName$(negChar)+". I'm interested in bringing"
   lineB$="you to "+namer$+" to work on my latest project..."
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)>0
   Speak(negStar,negVisitor,3)
   lineA$="Hi, "+charName$(negChar)+". Since we're friends, it would be great if you"
   lineB$="join me at "+namer$+" to work on my latest project!"
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)>0
   Speak(negStar,negVisitor,3)
   lineA$="Nice to see you again, "+charName$(negChar)+". I'm interested in bringing"
   lineB$="you to "+namer$+" to work on my latest project..."
  EndIf
  g=GetGender(negChar)
  ;If negTim=>350 And negChar=fedBooker(charFed(negChar)) Then negStage=1 : negTim=0 : keytim=20
  If negTim>350 And negTim<650 And negChar<>fedBooker(charFed(negChar))
   Speak(7,negStar,1)
   lineA$=charName$(negChar)+" is tied to "+fedName$(charFed(negChar))+" with a"
   lineB$=charContract(negChar)+"-"+textPeriod$(optTime)+" contract worth $"+GetFigure$(charSalary(negChar))+" per "+textPeriod$(optTime)+"!"
  EndIf
  If negTim>675 And negTim<975 And negChar<>fedBooker(charFed(negChar))
   Speak(7,negStar,1)
   lineA$="It'll cost at least $"+GetFigure$(negBuyOut)+" to buy "+Him$(g)
   lineB$="out of that contract, "+charName$(gamChar)+"..."
  EndIf
  If negTim>1000 And negTim<1300 And negChar<>fedBooker(charFed(negChar))
   Speak(negStar,7,3)
   If fedPolicy(charFed(gamChar))<>7 Then lineA$="OK, "+charName$(pChar(7))+" - I understand the situation!"
   If fedPolicy(charFed(gamChar))=7 Then lineA$="Don't worry - "+fedName$(charFed(gamChar))+" will take care of that!" : negBuyOut=0 : negPayOff=0
   lineB$="With that in mind, I'm prepared to offer this deal:"
  EndIf
  If negTim>350 And negTim<650 And negChar=fedBooker(charFed(negChar))
   Speak(negVisitor,negStar,1)
   lineA$="I'm committed to "+fedName$(charFed(negChar))+" with a"
   lineB$=charContract(negChar)+"-"+textPeriod$(optTime)+" contract worth $"+GetFigure$(charSalary(negChar))+" per "+textPeriod$(optTime)+"!"
  EndIf
  If negTim>675 And negTim<975 And negChar=fedBooker(charFed(negChar))
   Speak(negVisitor,negStar,1)
   lineA$="It'll cost at least $"+GetFigure$(negBuyOut)+" to buy me"
   lineB$="out of that contract, "+charName$(gamChar)+"..."
  EndIf
  If negTim>1000 And negTim<1300 And negChar=fedBooker(charFed(negChar))
   Speak(negStar,negVisitor,3)
   If fedPolicy(charFed(gamChar))<>7 Then lineA$="OK, "+charName$(negChar)+" - I understand the situation!"
   If fedPolicy(charFed(gamChar))=7 Then lineA$="Don't worry - "+fedName$(charFed(gamChar))+" will take care of that!" : negBuyOut=0 : negPayOff=0
   lineB$="With that in mind, I'm prepared to offer this deal:"
  EndIf
  If negTim>1325 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;collaboration offer
 If negStage=0 And negTopic=2
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)=<0
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", I'm a big fan of your work!"
   lineB$="How would you feel about collaborating with me?"
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)=0 And charRelationship(gamChar,negChar)>0
   Speak(negVisitor,negStar,3)
   lineA$="Hey,  "+charName$(gamChar)+", it's good to see you again!"
   lineB$="Since we're friends, why don't we collaborate?"
  EndIf
  If negTim>25 And negTim<325 And charExperience(negChar,0)>0
   Speak(negVisitor,negStar,3)
   lineA$="Hey, "+charName$(gamChar)+", do you remember me? I think"
   lineB$="it's about time we worked together again!"
  EndIf
  If negTim>350 And negTim<650
   Speak(negVisitor,negStar,3)
   lineA$="I'm sure we could create something special."
   lineB$="This is the arrangement I had in mind:"
  EndIf
  If negTim>650 Then camFoc=negStar
  If negTim>675 Then negStage=1 : negTim=0 : keytim=20
 EndIf
 ;don't listen to offer
 If negStage=1 And negTopic<>2 And charNegotiated(negChar)>0 Then negVerdict=1 : negStage=2 : negTim=0 : keytim=20
 ;--------------- CONTRACT SETUP --------------------
 foc=0
 If negStage=1 Or negStage=3
  ;facial expression
  mood=2
  If foc=5 Then mood=1
  If foc=4 Then mood=3
  Speak(negStar,negVisitor,mood)
  ;options
  y=455
  Color 0,0,0 : Rect rX#(0),rY#(y),rX#(800),rY#(600-y),1
  DrawOption(5,rX#(125),rY#(y+72),"<<< WITHDRAW <<<","")
  namer$="$"+GetFigure$(negPayOff)
  If negBuyOut>0 And negPayOff=>negBuyOut Then namer$="$"+GetFigure$(negBuyOut)+" + $"+GetFigure$(negPayOff-negBuyOut)
  DrawOption(1,rX#(400),rY#(y+32),"Advance",namer$)
  DrawOption(2,rX#(400),rY#(y+72),"Salary","$"+GetFigure$(negSalary))
  DrawOption(3,rX#(400),rY#(y+112),"Contract",negContract+" "+textPeriod$(optTime)+"s") 
  namer$=">>> PROPOSE >>>"
  If negTopic=2 And DealChanged()=0 Then namer$=">>> ACCEPT >>>"
  If negStage=3 Then namer$=">>> CONFIRM >>>"
  DrawOption(4,rX#(675),rY#(y+72),namer$,"")
  ;show profile
  ;If (foc=0 Or foc=>4) And MouseDown(2) Then DrawCharProfile(negChar,rX#(400),rY#(10),1)
  DrawCharProfile(negChar,rX#(400),rY#(5),1) 
 EndIf
 ;------------------ VERDICTS -----------------------------
 If negStage=2 And negTim>10 Then camFoc=negVisitor
 ;acceptances
 If negStage=2 And negVerdict=0
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   greenLineA$="OK, I'm happy to sign up for that contract!"
   greenLineB$="Put it down in writing and it's a done deal..."
  EndIf
  If negTim>350 Then negStage=3 : negTim=0 : keytim=15
 EndIf
 ;urgent objections
 If negStage=2 And negVerdict=1
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="We've already talked about this enough for one day!"
   redLineB$="Come back when you've given it some more thought..."
  EndIf
  If negTim>375 Then negStage=5 : go=1
 EndIf
 If negStage=2 And negVerdict=2
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3) 
   redLineA$="I'm flattered by your interest, but I'm too busy"
   redLineB$="running "+fedName$(charFed(negChar))+" to work on any projects!"
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=3
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="I'm not leaving "+fedName$(charFed(negChar))+" to slum it on the"
   redLineB$="independent scene! That's behind me now..."
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=4
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3) 
   redLineA$="Sorry, but I'm still contracted to "+fedName$(charFed(negChar))+"!"
   redLineB$="I won't be able to leave for another "+charContract(negChar)+" "+textPeriod$(optTime)+"s..."
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=5
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3) 
   redLineA$="Sorry, but I can't leave "+fedName$(charFed(negChar))+" right now."
   redLineB$="I'm one of the few people keeping the company afloat!"
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=6
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="There's no room for me at "+fedName$(charFed(gamChar))+"!"
   redLineB$="Don't go making promises you can't keep..."
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=7
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="There's no room for anybody else in your group!"
   redLineB$="You should be more careful about who you sign..."
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=8
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="There's no room for anybody else in your office!"
   redLineB$="Perhaps you should invest in another workstation..."
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=9
  If negTim>25 And negTim<325 And charTeam(negChar)=gamChar
   Speak(negVisitor,negStar,1) 
   redLineA$="Forget about it, "+charName$(gamChar)+"! I'm not"
   redLineB$="spending any longer with you than I have to!"
  EndIf
  If negTim>25 And negTim<325 And charTeam(negChar)<>gamChar
   Speak(negVisitor,negStar,1) 
   redLineA$="Forget about it, "+charName$(gamChar)+"! I wouldn't work"
   redLineB$="for an asshole like you if my life depended on it!"
  EndIf
  If negTim>375 Then negStage=5 : go=1
 EndIf 
 ;payment objections
 If negStage=2 And negVerdict=10
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="You don't even HAVE $"+GetFigure$(negPayOff-negBuyOut)+" to give me!"
   redLineB$="I'd rather not chase you up for money..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=11
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="You expect me to work for you for FREE?!"
   redLineB$="I should charge you for this waste of time!"
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=12
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="You don't even HAVE $"+GetFigure$(negSalary)+" to give me!"
   redLineB$="I don't want my first pay cheque to bounce..."
  EndIf
  If negTim>350 Then PushLuck(2) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=13
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="You may be surprised to hear that I'm already"
   redLineB$="earning more than that in my current role!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf

 If negStage=2 And negVerdict=16
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, the only way that would work is"
   redLineB$="if you added an advance payment as well?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=17
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, the only way that would work is"
   redLineB$="if you added a "+textPeriod$(optTime)+"ly salary as well?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf  
 If negStage=2 And negVerdict=18
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="I'm insulted you would even dare make such an offer!"
   redLineB$="You're obviously not taking this very seriously..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=19
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, that deal isn't going to work for me."
   redLineB$="If there's no advance, I'll need a better salary!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=20
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, that deal isn't going to work for me."
   redLineB$="Without a salary, I'll need a bigger advance!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=21
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, a "+negContract+"-"+textPeriod$(optTime)+" contract deserves more than that."
   redLineB$="You're asking me to make quite a commitment!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=22
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="What's the point in signing up for that amount"
   redLineB$="of time? We won't be able to accomplish anything!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=23
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, I'm not signing up for that deal."
   redLineB$="I think I deserve a better salary..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=24
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, I'm not signing up for that deal."
   redLineB$="I think I deserve a better advance..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=25
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, I'm not signing up for that deal."
   redLineB$="We both know I'm worth more than that!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=26
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="No, even a "+negContract+"-"+textPeriod$(optTime)+" contract deserves more than that."
   redLineB$="You're paying for the QUALITY - not the quantity!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 

 If negStage=2 And negVerdict=28
  If negTim>25 And negTim<325 And negPayOffPrefer=<3
   Speak(negVisitor,negStar,1) 
   redLineA$="No, I'm not happy with that arrangement. I'd like"
   redLineB$="at least "+(negPayOffPrefer*25)+"% of my payment to be a lump sum?"
  EndIf
  If negTim>25 And negTim<325 And negPayOffPrefer=>4
   Speak(negVisitor,negStar,1) 
   redLineA$="No, I'm not happy with that arrangement. I'd prefer"
   redLineB$="to receive every penny of my payment upfront!"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=29
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3) 
   redLineA$="Hmmm, that's a very tempting offer."
   redLineB$="Would you consider making it even better?"
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 ;personal objections
 If negStage=2 And negVerdict=31
  If negTim>25 And negTim<325 And charTeam(negChar)<>gamChar
   Speak(negVisitor,negStar,1) 
   redLineA$="Sorry, but I couldn't stand to be in the"
   redLineB$="same room as an asshole like "+charName$(negEnemy)+"!"
  EndIf
  If negTim>25 And negTim<325 And charTeam(negChar)=gamChar
   Speak(negVisitor,negStar,1) 
   redLineA$="Sorry, but I'm not working with that asshole"
   redLineB$=charName$(negEnemy)+" for any longer than I have to!"
  EndIf
  If negTim>350 Then PushLuck(99) : negTim=10 : keytim=15
 EndIf
 If negStage=2 And negVerdict=32
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="Sorry, but I feel someone like you should be working"
   redLineB$="for someone like me - not the other way around!"
  EndIf
  If negTim>350 Then PushLuck(2) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=33
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,2) 
   redLineA$="Thanks for the offer, but I prefer to work alone."
   redLineB$="I can't see myself as a cog in the machine..."
  EndIf
  If negTim>350 Then PushLuck(2) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=34
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3) 
   redLineA$="Thanks for the offer, but I'm happy as I am!"
   redLineB$="I don't see how you could improve my career..."
  EndIf
  If negTim>350 Then PushLuck(2) : negTim=10 : keytim=15
 EndIf 
 If negStage=2 And negVerdict=35
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1) 
   redLineA$="Sorry, but I haven't enjoyed working for you!"
   redLineB$="It would take a lot to convince me to stay..."
  EndIf
  If negTim>350 Then PushLuck(1) : negTim=10 : keytim=15
 EndIf 
 ;----------------- ENDINGS -----------------------
 If negStage=>4 And negTim>10 Then camFoc=negVisitor
 ;successful deal
 If negStage=4
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,3)
   If charged=0 Then gamBank=gamBank-negPayOff : PlaySound sCash : charged=1
   lineA$="I'm glad we could come to an arrangement!"
   lineB$="I look forward to the next "+negContract+" "+textPeriod$(optTime)+"s..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;deal breakdown
 If negStage=5 And charTeam(negChar)<>gamChar
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,1)
   redLineA$="Alright, this meeting isn't going anywhere."
   redLineB$="Let's just forget about it for the time being..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;renewal breakdown
 If negStage=5 And charTeam(negChar)=gamChar
  If negTim>25 And negTim<325
   Speak(negVisitor,negStar,2)
   redLineA$="Well, we're obviously not on the same page anymore."
   redLineB$="Let's just go our separate ways for the time being..."
  EndIf
  If negTim>375 Then go=1
 EndIf
 ;---------- DISPLAY SUBTITLES ----------
 DisplaySubtitles()
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
;update status
If negStage=4
 If charTeam(negChar)<>gamChar Then JoinTeam(negChar,gamChar) : charWorked(negChar)=gamNight
 charWorth(negChar)=negWorth
 charSalary(negChar)=negSalary
 charContract(negChar)=negContract
 ChangeRelationship(gamChar,negChar,1)
 If charHappiness(negChar)<0 Then charHappiness(negChar)=0
 charHappiness(negChar)=charHappiness(negChar)+1
Else
 If charTeam(negChar)=gamChar
  LeaveTeam(negChar)
  charRep(negChar)=charRep(negChar)-1
  charHappiness(negChar)=charHappiness(negChar)-1
 EndIf
EndIf
;proceed
charNegotiated(negChar)=1
screen=15
End Function

;////////////////////////////////////////////////////////////////////////////
;-------------------------- RELATED FUNCTIONS -------------------------------
;////////////////////////////////////////////////////////////////////////////

;CALCULATE % VALUE
Function GetValue(char)
 value=charRep(char)*4
 value=value+charDesign(char)+charProgram(char)+char2D(char)+char3D(char)+charSound(char)+charMusic(char)
 value=value/9
 If value>99 Then value=99 
 Return value
End Function

;CALCULATE WORTH (RAW SALARY)
Function CalculateWorth(char)
 ;base value
 chunk#=GetValue(char);-10
 If chunk#<25 Then chunk#=25
 ;magnifier#=chunk#/15
 ;If magnifier#<1 Then magnifier#=1
 ;value=Int((chunk#*chunk#)*magnifier#)
 value=chunk#*chunk#
 ;filters
 If charFed(char)=no_feds Then value=value/2 ;unemployed
 ;learning curve
 ;curve=(charAge(char)-10)*4
 ;If curve>100 Then curve=100
 ;value=Int(PercentOf#(value,curve))
 ;If game=1 And value>charWorth(char)*2 Then value=charWorth(char)*2
 ;limits
 value=RoundOff(value,10) 
 If value>1000 Then value=RoundOff(value,100)
 If value<100 Then value=100
 Return value
End Function

;GENERATE CPU CONTRACT
Function GenerateContract(char)
 If charFed(char)=no_feds And char<>fedBooker(no_feds) Then charContract(char)=0 Else charContract(char)=Rnd(8,48)
 charSalary(char)=CalculateWorth(char)
 charSalary(char)=Rnd(PercentOf#(charSalary(char),75),PercentOf#(charSalary(char),125))
 charSalary(char)=RoundOff(charSalary(char),10)
 If charSalary(char)>1000 Then charSalary(char)=RoundOff(charSalary(char),100)
 charRoyalties(char)=GetValue(char)/2
 If charFed(char)=no_feds Then charRoyalties(char)=50 
End Function

;FILTER VALUE THROUGH CONTRACT LENGTH
Function ContractFilter(value,contract)
 factor#=100
 ;negative swing
 If contract<24/optTime
  factor#=GetPercent#((24/optTime)-(Float#(24-contract)/3.0),24/optTime)
 EndIf
 ;positive swing
 If contract>24/optTime
  factor#=GetPercent#((24/optTime)+(Float#(contract-24)/4.0),24/optTime)
 EndIf
 ;translate
 value=Int(PercentOf#(value,factor#))
 Return value
End Function

;GET LABEL CONTRACT VERDICT
Function GetDealVerdict()
 ;translate values
 verdict=0
 negWorthStretch=ContractFilter(negWorth,negContract)
 ;negChunkStretch=ContractFilter(negChunk,negContract) 
 ;payment objections
 If negContract<24/optTime And negPayOff>(negWorthStretch*negTeamMultiple)*negContract And negPayOff=<(negWorth*negTeamMultiple)*negContract
  If negContract<>negInitialContract Then verdict=18 ;too much for short
 EndIf
 If negPayOff>negInitialPayOff And negPayOff>(negWorthStretch*negTeamMultiple)*negContract
  If negRoyalties<negChunk/2 Then verdict=12 Else verdict=11 ;advance too high
  If negPayOff=<negInitialPayOff And negContract<negInitialContract Then verdict=19 ;too much for shortened
 EndIf
 If negRoyalties>negInitialRoyalties And negRoyalties>negChunk
  If negPayOff<((negWorthStretch*negTeamMultiple)*negContract)/2 Then verdict=14 Else verdict=13 ;royalties too high
 EndIf
 If negPayOff>(negWorthStretch*negTeamMultiple)*negContract And negRoyalties>negChunk
  If negPayOff>negInitialPayOff And negRoyalties>negInitialRoyalties Then verdict=10 ;everything too high 
 EndIf
 If negPayOff>negInitialPayOff And negPayOff>((negWorthStretch*negTeamMultiple)*negContract)*2 Then verdict=15 ;advance way too high
 If negRoyalties>negInitialRoyalties And negRoyalties>(negChunk*2) Then verdict=16 ;royalties way too high
 ;If negPayOff>negInitialPayOff And negInitialPayOff=>(negWorthStretch*negTeamMultiple)*negContract Then verdict=21 ;ignored generous advance
 ;If negRoyalties>negInitialRoyalties And negInitialRoyalties=>negChunk Then verdict=22 ;ignored generous royalties
 ;urgent objections
 If negPayOff<((negWorthStretch*negTeamMultiple)*negContract)/2 And gamBank<((negWorthStretch*negTeamMultiple)*negContract)/2
  If negPayOff<negInitialPayOff Then verdict=20 ;not rich enough to forsake advance
 EndIf
 If negContract=<4/optTime And negContract<>negInitialContract Then verdict=17 ;way too short
 ;policies
 If negRoyalties>0 And fedPolicy(fed)=2 Then verdict=21 ;no royalties
 If negPayOff>0 And fedPolicy(fed)=3 Then verdict=22 ;no advances 
 If fedPolicy(fed)=4 And negContract<negInitialContract Then verdict=23 ;long-term
 If fedPolicy(fed)=5 And negContract>negInitialContract Then verdict=24 ;short-term
 Return verdict
End Function

;GET RECRUITMENT VERDICT
Function GetRecruitVerdict()
 ;translate values
 verdict=0
 negWorthStretch=ContractFilter(negWorth,negContract)
 negPayRequired=negWorthStretch*negContract 
 negPayTotal=(negSalary*negContract)+(negPayOff-negBuyOut)
 ;push luck on good contract!
 randy=Rnd(0,10)
 If randy=<1 And negChances>1 Then verdict=29 
 ;30-50: personal objections 
 If negChar=5 Or negChar=130
  If negPayTotal<negPayRequired*2 And charRelationship(negChar,gamChar)=<0 Then verdict=33 ;loner
 EndIf
 If negPayTotal<PercentOf#(negPayRequired,150) And negTopic<>2
   If charRelationship(negChar,gamChar)=<0
    If charTeam(negChar)=gamChar And charHappiness(negChar)<-1 Then verdict=35 ;not keen to renew
    If charTeam(negChar)<>gamChar And charHappiness(negChar)>1 Then verdict=34 ;happy where they are
    If charRep(negChar)=>charRep(gamChar)+10 Then verdict=32 ;bigger star
   EndIf
   ;won't stay with enemy
   negEnemy=0
   For count=1 To optTeamSize
    v=charRoster(gamChar,count)
    If charRelationship(negChar,v)<0 And v<>gamChar Then negEnemy=v : verdict=31 
   Next
 EndIf
 ;10-30: payment objections
 If charContract(negChar)>0 And negPayTotal<charSalary(negChar)*negContract Then verdict=13 ;already earning more
 If negPayTotal<negPayRequired
  Repeat
   randy=Rnd(2,10)
   If randy=10 And negContract=>24/optTime Then verdict=21 ;not enough for longer contract
   If randy=9 And negContract<24/optTime Then verdict=26 ;too little even for shorter contract 
   If randy=8 And negPayOff-negBuyOut=0 Then verdict=16 ;no advance so add one
   If randy=7 And negSalary=0 Then verdict=17 ;no salary so add one
   If randy=6 And negPayOff-negBuyOut=0 Then verdict=19 ;no advance so increase salary
   If randy=5 And negSalary=0 Then verdict=20 ;no salary so increase pay-off
   If randy=4 And negPayOff-negBuyOut>0 Then verdict=24 ;pay-off exists but is too low
   If randy=3 And negSalary>0 Then verdict=23 ;salary exists but is too low
   If randy=2 Then verdict=25 ;payment is too low
  Until verdict=>1 And verdict=<40 
  If negContract>24/optTime And negPayTotal=>negWorth*negContract Then verdict=21 ;not quite enough for longer contract
  If negPayOffPrefer>0 And negPayOff-negBuyOut<PercentOf#(negPayRequired,negPayOffPrefer*25) And negPayTotal<PercentOf#(negPayRequired,150)
   verdict=28 ;insists on pay-off
  EndIf
  If negContract=<4/optTime And negPayTotal<PercentOf#(negPayRequired,150) Then verdict=22 ;contract way too short
  If negPayTotal=<negPayRequired/2 Then verdict=18 ;insultingly low
 EndIf
 If negPayOff-negBuyOut=0 And negSalary=0 Then verdict=11 ;slave labour!
 If negPayOff-negBuyOut>0 And negPayOff>gamBank Then verdict=10 ;can't afford pay-off
 If negSalary>0 And negSalary>gamBank Then verdict=12 ;can't afford salary
 ;1-10: urgent objections
 If negTopic<>2
  If charTeam(negChar)<>gamChar
   If TeamSize(gamChar)=>CountDesks() Then verdict=8 ;no room in office
   If TeamSize(gamChar)=>optTeamSize Then verdict=7 ;no room in team
  EndIf
  If charFed(negChar)<>charFed(gamChar) And fedSize(charFed(gamChar))=>optRosterLim Then verdict=6 ;no room at label 
  If charFed(negChar)<>charFed(gamChar) And fedSize(charFed(negChar))=<3 Then verdict=5 ;can't leave depleted label 
  ;If charFed(negChar)<>charFed(gamChar) And charContract(negChar)>1 Then verdict=4 ;contracted to another label 
  If negPayTotal<negPayRequired*2
   If charFed(negChar)<no_feds And charFed(gamChar)=no_feds Then verdict=3 ;won't join underground!
   If negChar=fedBooker(charFed(negChar)) Then verdict=2 ;too busy running label
   If charRelationship(negChar,gamChar)<0 Then verdict=9 ;hates you!
  EndIf
  If charNegotiated(negChar)=1 Then verdict=1 ;already tried
 EndIf
 ;guaranteed if approached
 If negTopic=2 And negContract=negInitialContract And negPayOff=>negInitialPayOff And negSalary=>negInitialSalary
  verdict=0
 EndIf
 Return verdict
End Function

;LOSE CHANCES
Function PushLuck(degree)
 negChances=negChances-degree
 If negChances>0 Then negStage=1 Else negStage=5
End Function

;DEAL CHANGED?
Function DealChanged()
 value=0
 If negPayOff<>negInitialPayOff Then value=1
 If negSalary<>negInitialSalary Then value=1
 If negRoyalties<>negInitialRoyalties Then value=1
 If negContract<>negInitialContract Then value=1
 Return value
End Function

;GET BEST FIGURE CHANGER
Function FigureRange(amount)
 ;negative values
 If amount<0 Then value=-10
 If amount=<-1000 Then value=-100
 If amount=<-10000 Then value=-1000
 If amount=<-100000 Then value=-10000
 If amount=<-1000000 Then value=-50000
 If amount=<-10000000 Then value=-100000
 ;positive values
 If amount=>0 Then value=10
 If amount=>1000 Then value=100
 If amount=>10000 Then value=1000
 If amount=>100000 Then value=10000
 If amount=>1000000 Then value=50000
 If amount=>10000000 Then value=100000
 Return value
End Function