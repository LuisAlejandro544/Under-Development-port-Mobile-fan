;//////////////////////////////////////////////////////////////////////////////
;--------------------------- UNDER DEVELOPMENT: DATA --------------------------
;//////////////////////////////////////////////////////////////////////////////

;------------------------------------------------------------------------
;///////////////////////////// OPTIONS //////////////////////////////////
;------------------------------------------------------------------------

;SAVE OPTIONS
Function SaveOptions()
 file=WriteFile("Data/Options.dat")
  WriteInt file,optStartDate
  WriteInt file,optTime
  WriteInt file,optJumble
  WriteInt file,optCutscenes
  WriteInt file,optEntourage
  WriteInt file,optMusicLoad
  WriteInt file,optMusicVolume
  WriteInt file,optMenuRes
 CloseFile(file)
End Function

;LOAD OPTIONS
Function LoadOptions()
 file=ReadFile("Data/Options.dat")
  optStartDate=ReadInt(file)
  optTime=ReadInt(file)
  optJumble=ReadInt(file)
  optCutscenes=ReadInt(file)
  optEntourage=ReadInt(file)
  optMusicLoad=ReadInt(file)
  optMusicVolume=ReadInt(file)
  optMenuRes=ReadInt(file)
 CloseFile(file)
End Function

;------------------------------------------------------------------------
;////////////////////////////// UNIVERSE ////////////////////////////////
;------------------------------------------------------------------------

;SAVE UNIVERSE
Function SaveUniverse()
 file=WriteFile("Data/Universe.dat")
  ;slot status
  For cyc=1 To 3
   WriteInt file,slotActive(cyc)
   WriteString file,slotName$(cyc)
   WriteInt file,slotDate(cyc)
  Next
  ;hi scores
  For cyc=1 To 10
   WriteInt file,hiChar(cyc)
   WriteString file,hiName$(cyc)
   WriteFloat file,hiPhotoHeight#(cyc)
   WriteInt file,hiPhotoR(cyc)
   WriteInt file,hiPhotoG(cyc)
   WriteInt file,hiPhotoB(cyc)
   WriteInt file,hiWealth(cyc)
   WriteInt file,hiRep(cyc)
   WriteInt file,hiExperience(cyc)
   WriteInt file,hiFed(cyc)
  Next
 CloseFile(file)
End Function

;LOAD UNIVERSE
Function LoadUniverse()
 Loader("","")
 file=ReadFile("Data/Universe.dat")
  ;slot status
  For cyc=1 To 3
   slotActive(cyc)=ReadInt(file)
   slotName$(cyc)=ReadString(file)
   slotDate(cyc)=ReadInt(file)
   ;slotPreview(cyc)=LoadImage("Data/Previews/Preview0"+cyc+".bmp")
   ;MaskImage slotPreview(cyc),255,0,255
  Next
  ;hi scores
  For cyc=1 To 10
   hiChar(cyc)=ReadInt(file)
   hiName$(cyc)=ReadString(file)
   hiPhotoHeight#(cyc)=ReadFloat(file)
   hiPhotoR(cyc)=ReadInt(file)
   hiPhotoG(cyc)=ReadInt(file)
   hiPhotoB(cyc)=ReadInt(file)
   hiPhoto(cyc)=LoadImage("Data/Hall Of Fame/Photo"+Dig$(cyc,10)+".bmp")
   MaskImage hiPhoto(cyc),hiPhotoR(cyc),hiPhotoG(cyc),hiPhotoB(cyc) 
   hiWealth(cyc)=ReadInt(file)
   hiRep(cyc)=ReadInt(file)
   hiExperience(cyc)=ReadInt(file)
   hiFed(cyc)=ReadInt(file)
  Next
 CloseFile(file)
End Function

;------------------------------------------------------------------------
;///////////////////////////// PROGRESS /////////////////////////////////
;------------------------------------------------------------------------

;SAVE PROGRESS
Function SaveProgress(directory)
 If directory<0 Then file=WriteFile("Data/Backup/Progress.dat")
 If directory=>0 Then file=WriteFile("Data/Slot0"+directory+"/Progress.dat")
  ;career status
  WriteInt file,gamChar
  WriteInt file,gamDeskPosition
  For cyc=1 To optTeamSize
   WriteInt file,gamDesk(cyc)
  Next
  WriteInt file,gamBank
  WriteInt file,gamMillionaire
  WriteInt file,gamTax
  WriteInt file,gamRent
  WriteInt file,gamRepairCost
  WriteInt file,gamFashion
  ;schedule
  WriteInt file,gamDate 
  WriteInt file,gamMission
  WriteInt file,gamTarget
  WriteInt file,gamDeadline
  ;agreements  
  For cyc=1 To 50
   WriteInt file,gamAgreement(cyc)
  Next
  ;projects
  WriteInt file,no_projects
  WriteInt file,gamProject
  WriteInt file,gamHardware
  WriteInt file,gamRelease
  For cyc=1 To 100
	WriteString file,projTitle$(cyc)
	WriteInt file,projIdentity(cyc)
	WriteInt file,projIdentified(cyc)
	WriteInt file,projFormat(cyc)
	WriteInt file,projLabel(cyc)
	WriteInt file,projReleased(cyc)
	WriteInt file,projDate(cyc)
	WriteInt file,projReview(cyc)
	WriteInt file,projSales(cyc)
	WriteInt file,projPirated(cyc)  
	WriteInt file,projGameplay(cyc)
	WriteInt file,projReplay(cyc)
	WriteInt file,proj2D(cyc)
	WriteInt file,proj3D(cyc)
	WriteInt file,projSound(cyc)
	WriteInt file,projMusic(cyc)
	WriteInt file,projOldGameplay(cyc)
	WriteInt file,projOldReplay(cyc)
	WriteInt file,projOld2D(cyc)
	WriteInt file,projOld3D(cyc)
	WriteInt file,projOldSound(cyc)
	WriteInt file,projOldMusic(cyc)
    For v=1 To 6
	 WriteInt file,projHiIncrease(cyc,v)
	 WriteInt file,projCredit(cyc,v)
	 WriteInt file,projLocked(cyc,v)
    Next
  Next
  ;hi scores 
  For cyc=1 To 20
   WriteInt file,gamHiScore(cyc)
   WriteInt file,gamHiCredit(cyc) 
  Next
  ;charts
  For cyc=1 To 20
   WriteInt file,chartList(cyc)
   WriteInt file,chartOldList(cyc)
  Next
  For cyc=1 To 50
   WriteInt file,chartChar(cyc)
   WriteInt file,chartCompany(cyc)
   WriteInt file,chartGame(cyc)
   WriteInt file,chartFormat(cyc)
   WriteFloat file,chartPromotion#(cyc)
   WriteInt file,chartSales(cyc)
   WriteInt file,chartRank(cyc)
   WriteInt file,chartOldRank(cyc)
  Next
 CloseFile(file)
End Function

;LOAD PROGRESS
Function LoadProgress(directory)
 Loader("","")
 If directory<0 Then file=ReadFile("Data/Backup/Progress.dat")
 If directory=>0 Then file=ReadFile("Data/Slot0"+directory+"/Progress.dat")
  ;career status
  gamChar=ReadInt(file)
  gamDeskPosition=ReadInt(file)
  For cyc=1 To optTeamSize
   gamDesk(cyc)=ReadInt(file)
  Next
  gamBank=ReadInt(file)
  gamMillionaire=ReadInt(file)
  gamTax=ReadInt(file)
  gamRent=ReadInt(file)
  gamRepairCost=ReadInt(file)
  gamFashion=ReadInt(file)
  ;schedule
  gamDate=ReadInt(file)
  gamMission=ReadInt(file)
  gamTarget=ReadInt(file)
  gamDeadline=ReadInt(file)
  ;agreements  
  For cyc=1 To 50
   gamAgreement(cyc)=ReadInt(file)
  Next
  ;projects
  no_projects=ReadInt(file)
  gamProject=ReadInt(file)
  gamHardware=ReadInt(file)
  gamRelease=ReadInt(file)
  For cyc=1 To 100
	projTitle$(cyc)=ReadString(file)
	projIdentity(cyc)=ReadInt(file)
	If projIdentity(cyc)>no_games Then projIdentity(cyc)=0  
	projIdentified(cyc)=ReadInt(file)
	If projIdentified(cyc)>no_games Then projIdentified(cyc)=0  
	projFormat(cyc)=ReadInt(file)
	projLabel(cyc)=ReadInt(file)
	projReleased(cyc)=ReadInt(file)
	projDate(cyc)=ReadInt(file)
	projReview(cyc)=ReadInt(file)
	projSales(cyc)=ReadInt(file)
	projPirated(cyc)=ReadInt(file)  
	projGameplay(cyc)=ReadInt(file)
	projReplay(cyc)=ReadInt(file)
	proj2D(cyc)=ReadInt(file)
	proj3D(cyc)=ReadInt(file)
	projSound(cyc)=ReadInt(file)
	projMusic(cyc)=ReadInt(file)
	projOldGameplay(cyc)=ReadInt(file)
	projOldReplay(cyc)=ReadInt(file)
	projOld2D(cyc)=ReadInt(file)
	projOld3D(cyc)=ReadInt(file)
	projOldSound(cyc)=ReadInt(file)
	projOldMusic(cyc)=ReadInt(file)
    For v=1 To 6
	 projHiIncrease(cyc,v)=ReadInt(file)
	 projCredit(cyc,v)=ReadInt(file)
	 projLocked(cyc,v)=ReadInt(file)
    Next
  Next
  ;hi scores 
  For cyc=1 To 20
   gamHiScore(cyc)=ReadInt(file)
   gamHiCredit(cyc)=ReadInt(file) 
  Next
  ;charts
  For cyc=1 To 20
   chartList(cyc)=ReadInt(file)
   chartOldList(cyc)=ReadInt(file)
  Next
  For cyc=1 To 50
   chartChar(cyc)=ReadInt(file)
   chartCompany(cyc)=ReadInt(file)
   chartGame(cyc)=ReadInt(file)
   If chartGame(cyc)>no_games Then chartGame(cyc)=0
   chartFormat(cyc)=ReadInt(file)
   chartPromotion#(cyc)=ReadFloat(file)
   chartSales(cyc)=ReadInt(file)
   chartRank(cyc)=ReadInt(file)
   chartOldRank(cyc)=ReadInt(file)
  Next
 CloseFile(file)
End Function

;------------------------------------------------------------------------
;////////////////////////////// WORLD ///////////////////////////////////
;------------------------------------------------------------------------

;SAVE WORLD
Function SaveWorld(directory)
 If directory<0 Then file=WriteFile("Data/Backup/World.dat")
 If directory=>0 Then file=WriteFile("Data/Slot0"+directory+"/World.dat") 
  ;companies
  WriteInt file,no_chars
  For cyc=1 To no_feds
   WriteString file,fedName$(cyc)
   WriteInt file,fedBank(cyc)
   WriteInt file,fedPopularity(cyc)
   WriteInt file,fedReputation(cyc)
   WriteInt file,fedOldPopularity(cyc)
   WriteInt file,fedOldReputation(cyc) 
   WriteInt file,fedBooker(cyc)
   WriteInt file,fedSize(cyc)
   For count=1 To 50
    WriteInt file,fedRoster(cyc,count)
   Next
   WriteInt file,fedPolicy(cyc)
   WriteString file,fedBrand$(cyc)
   WriteInt file,fedList(cyc)
   WriteInt file,fedOldList(cyc)
   WriteInt file,fedRanked(cyc)
   WriteInt file,fedOldRanked(cyc)
  Next
  ;leading characters
  For cyc=1 To 10
   WriteInt file,charLeader(cyc)
  Next
  ;hardware
  WriteInt file,hardLeader(0)
  WriteInt file,hardOldLeader
  For cyc=1 To 50
   WriteInt file,hardFormat(cyc)
   WriteInt file,hardCost(cyc)
   WriteInt file,hardRetail(cyc)
   WriteInt file,hardOwned(cyc)
   WriteInt file,hardWeek(cyc)
   WriteInt file,hardMonth(cyc)
   WriteInt file,hardYear(cyc)
   WriteInt file,hardReleased(cyc)
   WriteFloat file,hardPromotion#(cyc)
   WriteFloat file,hardOldPromotion#(cyc)
   WriteInt file,hardUsers(cyc)
   WriteInt file,hardPeakUsers(cyc)
   WriteInt file,hardPirated(cyc)
   WriteInt file,hardGameplay(cyc)
   WriteInt file,hardReplay(cyc)
   WriteInt file,hard2D(cyc)
   WriteInt file,hard3D(cyc)
   WriteInt file,hardSound(cyc)
   WriteInt file,hardMusic(cyc)
   WriteInt file,hardOldGameplay(cyc)
   WriteInt file,hardOldReplay(cyc)
   WriteInt file,hardOld2D(cyc)
   WriteInt file,hardOld3D(cyc)
   WriteInt file,hardOldSound(cyc)
   WriteInt file,hardOldMusic(cyc)
  Next
  ;formats
  For cyc=0 To 5
   WriteInt file,formatCost(cyc)
  Next
 CloseFile(file)
End Function

;LOAD WORLD
Function LoadWorld(directory)
 Loader("","")
 If directory<0 Then file=ReadFile("Data/Backup/World.dat")
 If directory=>0 Then file=ReadFile("Data/Slot0"+directory+"/World.dat")
  ;companies
  no_chars=ReadInt(file)
  For cyc=1 To no_feds
   If directory>0 Or oldSlot>0 Then fedName$(cyc)=ReadString(file) Else void=ReadString(file)
   fedBank(cyc)=ReadInt(file)
   fedPopularity(cyc)=ReadInt(file)
   fedReputation(cyc)=ReadInt(file)
   fedOldPopularity(cyc)=ReadInt(file)
   fedOldReputation(cyc)=ReadInt(file) 
   fedBooker(cyc)=ReadInt(file)
   fedSize(cyc)=ReadInt(file)
   For count=1 To 50
    fedRoster(cyc,count)=ReadInt(file)
   Next
   fedPolicy(cyc)=ReadInt(file)
   fedBrand$(cyc)=ReadString(file)
   fedList(cyc)=ReadInt(file)
   fedOldList(cyc)=ReadInt(file)
   fedRanked(cyc)=ReadInt(file)
   fedOldRanked(cyc)=ReadInt(file)
  Next
  ;leading characters
  For cyc=1 To 10
   charLeader(cyc)=ReadInt(file)
  Next
  ;hardware
 If directory>0 Or oldSlot>0
  hardLeader(0)=ReadInt(file)
  hardOldLeader=ReadInt(file)
  For cyc=1 To 50
   hardFormat(cyc)=ReadInt(file)
   hardCost(cyc)=ReadInt(file)
   hardRetail(cyc)=ReadInt(file)
   hardOwned(cyc)=ReadInt(file)
   hardWeek(cyc)=ReadInt(file)
   hardMonth(cyc)=ReadInt(file)
   hardYear(cyc)=ReadInt(file)
   hardReleased(cyc)=ReadInt(file)
   hardPromotion#(cyc)=ReadFloat(file)
   hardOldPromotion#(cyc)=ReadFloat(file)
   hardUsers(cyc)=ReadInt(file)
   hardPeakUsers(cyc)=ReadInt(file)
   hardPirated(cyc)=ReadInt(file)
   hardGameplay(cyc)=ReadInt(file)
   hardReplay(cyc)=ReadInt(file)
   hard2D(cyc)=ReadInt(file)
   hard3D(cyc)=ReadInt(file)
   hardSound(cyc)=ReadInt(file)
   hardMusic(cyc)=ReadInt(file)
   hardOldGameplay(cyc)=ReadInt(file)
   hardOldReplay(cyc)=ReadInt(file)
   hardOld2D(cyc)=ReadInt(file)
   hardOld3D(cyc)=ReadInt(file)
   hardOldSound(cyc)=ReadInt(file)
   hardOldMusic(cyc)=ReadInt(file)
  Next
  ;formats
  For cyc=0 To 5
   formatCost(cyc)=ReadInt(file)
  Next
 EndIf
 CloseFile(file)
End Function

;------------------------------------------------------------------------
;//////////////////////////// CHARACTERS ////////////////////////////////
;------------------------------------------------------------------------

;SAVE ALL CHARACTERS
Function SaveChars(directory)
 For char=1 To no_chars
  SaveChar(char,directory)
 Next
End Function

;SAVE INDIVIDUAL CHARACTER
Function SaveChar(char,directory)
  CheckLimits(char)
  If directory<0 Then file=WriteFile("Data/Backup/Character"+Dig$(char,100)+".dat") 
  If directory=>0 Then file=WriteFile("Data/Slot0"+directory+"/Character"+Dig$(char,100)+".dat")
   ;profile 
   WriteString file,charName$(char)
   WriteString file,charTeamName$(char)
   WriteInt file,charTeam(char)
   For count=1 To optTeamSize
    WriteInt file,charRoster(char,count)
   Next
   WriteInt file,charPhotoR(char)
   WriteInt file,charPhotoG(char)
   WriteInt file,charPhotoB(char)
   ;attributes
   WriteInt file,charDesign(char)
   WriteInt file,charProgram(char)
   WriteInt file,char2D(char)
   WriteInt file,char3D(char)
   WriteInt file,charSound(char)
   WriteInt file,charMusic(char)
   WriteInt file,charRep(char)
   WriteInt file,charOldDesign(char)
   WriteInt file,charOldProgram(char)
   WriteInt file,charOld2D(char)
   WriteInt file,charOld3D(char)
   WriteInt file,charOldSound(char)
   WriteInt file,charOldMusic(char)
   WriteInt file,charOldRep(char)
   ;build
   WriteInt file,charAge(char)
   WriteInt file,charHeight(char)
   WriteInt file,charBuild(char)
   WriteInt file,charEyes(char)
   WriteInt file,charHatStyle(char)
   WriteInt file,charHat(char)
   WriteInt file,charSpecs(char)
   WriteInt file,charAccessory(char)
   ;clothing
   WriteInt file,charHairStyle(char)
   WriteInt file,charHair(char)
   WriteInt file,charFace(char)
   WriteInt file,charBeard(char)
   WriteInt file,charBody(char)
   WriteInt file,charArms(char)
   WriteInt file,charHands(char)
   WriteInt file,charShorts(char)
   WriteInt file,charLegs(char)
   WriteInt file,charShoes(char)
   WriteInt file,charBaggy(char)
   ;career status
   WriteInt file,charFed(char)
   For count=0 To no_feds
    WriteInt file,charExperience(char,count)
   Next
   WriteInt file,charWorth(char)
   WriteInt file,charContract(char)
   WriteInt file,charSalary(char)
   WriteInt file,charRoyalties(char)
   WriteInt file,charVacant(char)
   WriteInt file,charWorked(char)
   WriteInt file,charDedicated(char)
   WriteInt file,charNegotiated(char)
   WriteInt file,charHappiness(char)
   ;relationships
   WriteInt file,charTradeReaction(char)
   For count=1 To optCharLim
    WriteInt file,charRelationship(char,count) 
   Next
   ;agreements
   For count=1 To 50
    WriteInt file,charAgreement(char,count) 
   Next
  CloseFile(file)
End Function

;LOAD ALL CHARACTERS
Function LoadChars(directory)
 ;restore individual data
 For char=1 To no_chars
  LoadChar(char,directory)
 Next 
 ;confirm feds
 For f=1 To no_feds
  For count=1 To fedSize(f)
   charFed(fedRoster(f,count))=f
  Next
 Next
 ;check limits
 For char=1 To no_chars
  CheckLimits(char)
 Next
End Function

;LOAD INDIVIDUAL CHARACTER
Function LoadChar(char,directory)
  ;Loader("","")
  If directory<0 Then file=ReadFile("Data/Backup/Character"+Dig$(char,100)+".dat") 
  If directory=>0 Then file=ReadFile("Data/Slot0"+directory+"/Character"+Dig$(char,100)+".dat")
   ;profile 
   charName$(char)=ReadString(file)
   charTeamName$(char)=ReadString(file)
   charTeam(char)=ReadInt(file)
   For count=1 To optTeamSize
    charRoster(char,count)=ReadInt(file)
   Next
   charPhotoR(char)=ReadInt(file)
   charPhotoG(char)=ReadInt(file)
   charPhotoB(char)=ReadInt(file)
   ;attributes
   charDesign(char)=ReadInt(file)
   charProgram(char)=ReadInt(file)
   char2D(char)=ReadInt(file)
   char3D(char)=ReadInt(file)
   charSound(char)=ReadInt(file)
   charMusic(char)=ReadInt(file)
   charRep(char)=ReadInt(file)
   charOldDesign(char)=ReadInt(file)
   charOldProgram(char)=ReadInt(file)
   charOld2D(char)=ReadInt(file)
   charOld3D(char)=ReadInt(file)
   charOldSound(char)=ReadInt(file)
   charOldMusic(char)=ReadInt(file)
   charOldRep(char)=ReadInt(file)
   ;build
   charAge(char)=ReadInt(file)
   charHeight(char)=ReadInt(file)
   charBuild(char)=ReadInt(file)
   charEyes(char)=ReadInt(file)
   charHatStyle(char)=ReadInt(file)
   charHat(char)=ReadInt(file)
   charSpecs(char)=ReadInt(file)
   charAccessory(char)=ReadInt(file)
   ;clothing
   charHairStyle(char)=ReadInt(file)
   charHair(char)=ReadInt(file)
   charFace(char)=ReadInt(file)
   charBeard(char)=ReadInt(file)
   charBody(char)=ReadInt(file)
   charArms(char)=ReadInt(file)
   charHands(char)=ReadInt(file)
   charShorts(char)=ReadInt(file)
   charLegs(char)=ReadInt(file)
   charShoes(char)=ReadInt(file)
   charBaggy(char)=ReadInt(file)
   ;career status
   charFed(char)=ReadInt(file)
   For count=0 To no_feds
    charExperience(char,count)=ReadInt(file)
   Next
   charWorth(char)=ReadInt(file)
   charContract(char)=ReadInt(file)
   charSalary(char)=ReadInt(file)
   charRoyalties(char)=ReadInt(file)
   charVacant(char)=ReadInt(file)
   charWorked(char)=ReadInt(file)
   charDedicated(char)=ReadInt(file)
   charNegotiated(char)=ReadInt(file)
   charHappiness(char)=ReadInt(file)
   ;relationships
   charTradeReaction(char)=ReadInt(file)   
   For count=1 To optCharLim
    charRelationship(char,count)=ReadInt(file) 
   Next
   ;agreements
   For count=1 To 50
    charAgreement(char,count)=ReadInt(file) 
   Next
  CloseFile(file)
End Function

;LOAD PHOTOS
Function LoadPhotos(directory)
 Loader("Please Wait","Loading Characters")
 For char=0 To no_chars
  ;Loader("","")
  filer$="Data/Backup/Portraits/"
  If directory=>0 Then filer$="Data/Slot0"+slot+"/Portraits/"
  charPhoto(char)=LoadImage(filer$+"Photo"+Dig$(char,100)+".bmp")
  If charPhoto(char)=0
   charPhoto(char)=CopyImage(charPhoto(0))
   SaveImage(charPhoto(char),filer$+"Portraits/Photo"+Dig$(char,100)+".bmp")
   charPhotoR(char)=25 : charPhotoG(char)=5 : charPhotoB(char)=5
  EndIf
  If char=0 Then charPhotoR(char)=25 : charPhotoG(char)=5 : charPhotoB(char)=5
  MaskImage charPhoto(char),charPhotoR(char),charPhotoG(char),charPhotoB(char) 
 Next 
End Function

;SAVE PHOTOS
Function SavePhotos(directory)
 For char=1 To no_chars
  If directory<0 Then SaveImage(charPhoto(char),"Data/Backup/Portraits/Photo"+Dig$(char,100)+".bmp")
  If directory=>0 Then SaveImage(charPhoto(char),"Data/Slot0"+slot+"/Portraits/Photo"+Dig$(char,100)+".bmp")
 Next 
End Function

;-----------------------------------------------------------------
;////////////////////// RELATED FUNCTIONS ////////////////////////
;-----------------------------------------------------------------

;CHECK CHARACTER LIMITS
Function CheckLimits(char)
 ;profile
 If charTeam(char)<0 Then charTeam(char)=no_chars
 If charTeam(char)>no_chars Then charTeam(char)=0
 If charFed(char)<1 Then charFed(char)=no_feds
 If charFed(char)>no_feds Then charFed(char)=1
 If charContract(char)<0 Then charContract(char)=0
 If charContract(char)>48 Then charContract(char)=48
 If charSalary(char)<0 Then charSalary(char)=0
 If charSalary(char)>25000 Then charSalary(char)=25000
 ;attributes
 If charDesign(char)<25 Then charDesign(char)=25
 If charDesign(char)>99 Then charDesign(char)=99
 If charProgram(char)<25 Then charProgram(char)=25
 If charProgram(char)>99 Then charProgram(char)=99
 If char2D(char)<25 Then char2D(char)=25
 If char2D(char)>99 Then char2D(char)=99
 If char3D(char)<25 Then char3D(char)=25
 If char3D(char)>99 Then char3D(char)=99
 If charSound(char)<25 Then charSound(char)=25
 If charSound(char)>99 Then charSound(char)=99
 If charMusic(char)<25 Then charMusic(char)=25
 If charMusic(char)>99 Then charMusic(char)=99
 If charRep(char)<25 Then charRep(char)=25
 If charRep(char)>99 Then charRep(char)=99 
 ;build
 If charAge(char)<12 Then charAge(char)=12
 If charAge(char)>75 Then charAge(char)=75
 If charHeight(char)<0 Then charHeight(char)=0
 If charHeight(char)>24 Then charHeight(char)=24
 If charBuild(char)<0 Then charBuild(char)=no_models
 If charBuild(char)>no_models Then charBuild(char)=0 
 If charEyes(char)<1 Then charEyes(char)=3
 If charEyes(char)>3 Then charEyes(char)=1
 If charHatStyle(char)<0 Then charHatStyle(char)=no_hatstyles
 If charHatStyle(char)>no_hatstyles Then charHatStyle(char)=0
 If charHat(char)<1 Then charHat(char)=no_hats
 If charHat(char)>no_hats Then charHat(char)=1 
 If charSpecs(char)<0 Then charSpecs(char)=no_specs
 If charSpecs(char)>no_specs Then charSpecs(char)=0 
 If charAccessory(char)<0 Then charAccessory(char)=no_accessories
 If charAccessory(char)>no_accessories Then charAccessory(char)=0 
 ;costumes
 If charHairStyle(char)<0 Then charHairStyle(char)=no_hairstyles
 If charHairStyle(char)>no_hairstyles Then charHairStyle(char)=0 
 If charHair(char)<1 Then charHair(char)=no_hairs
 If charHair(char)>no_hairs Then charHair(char)=1	
 If charFace(char)<1 Then charFace(char)=no_faces
 If charFace(char)>no_faces Then charFace(char)=1
 If charBeard(char)<0 Then charBeard(char)=no_beards
 If charBeard(char)>no_beards Then charBeard(char)=0 
 If charBody(char)<1 Then charBody(char)=no_bodies
 If charBody(char)>no_bodies Then charBody(char)=1
 If charArms(char)<1 Then charArms(char)=no_arms
 If charArms(char)>no_arms Then charArms(char)=1
 If charHands(char)<1 Then charHands(char)=no_hands
 If charHands(char)>no_hands Then charHands(char)=1
 If charShorts(char)<0 Then charShorts(char)=no_legs
 If charShorts(char)>no_legs Then charShorts(char)=0
 If charLegs(char)<1 Then charLegs(char)=no_legs
 If charLegs(char)>no_legs Then charLegs(char)=1
 If charShoes(char)<1 Then charShoes(char)=no_legs
 If charShoes(char)>no_legs Then charShoes(char)=1 
 If charBaggy(char)<0 Then charBaggy(char)=3
 If charBaggy(char)>3 Then charBaggy(char)=0
End Function

;COPY CHARACTER DATA
Function CopyChar(source,target)
 ;profile
 charName$(target)=charName$(source)
 charContract(target)=charContract(source)
 charSalary(target)=charSalary(source)
 ;attributes
 charDesign(target)=charDesign(source)
 charProgram(target)=charProgram(source)
 char2D(target)=char2D(source) 
 char3D(target)=char3D(source)
 charSound(target)=charSound(source)
 charMusic(target)=charMusic(source)
 charRep(target)=charRep(source)
 ;build
 charAge(target)=charAge(source)
 charHeight(target)=charHeight(source)
 charBuild(target)=charBuild(source)
 charEyes(target)=charEyes(source)
 charHatStyle(target)=charHatStyle(source)
 charHat(target)=charHat(source)
 charSpecs(target)=charSpecs(source)
 charAccessory(target)=charAccessory(source)
 ;costumes
 charHairStyle(target)=charHairStyle(source)
 charHair(target)=charHair(source)
 charFace(target)=charFace(source)
 charBeard(target)=charBeard(source) 
 charBody(target)=charBody(source)
 charArms(target)=charArms(source)
 charHands(target)=charHands(source)
 charShorts(target)=charShorts(source)
 charLegs(target)=charLegs(source)
 charShoes(target)=charShoes(source)
 charBaggy(target)=charBaggy(source)
End Function