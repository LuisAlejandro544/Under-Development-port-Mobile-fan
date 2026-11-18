;//////////////////////////////////////////////////////////////////////////////
;------------------------- UNDER DEVELOPMENT: VARIABLES -----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////
;------------------ STRUCTURE -------------------------
;//////////////////////////////////////////////////////
Global version=1
Global screen,oldScreen
;1=editing, 2=develop, 3=recruiting, 4=aftermath, 5=career initiation, 6=identification, 7=relationships, 8=hardware marvel
Global screenState,screenAgenda
Global screenConfirm 
Global screenCall,callX,callY
Global go,gotim
Global foc,subFoc
Global timer,keytim
Global file
Global loadMessageA$,loadMessageB$

;//////////////////////////////////////////////////////
;------------------- OPTIONS --------------------------
;//////////////////////////////////////////////////////
.Options
;constants
Global optPlayLim=20,optCharLim=165,optRosterLim=27,optTeamSize=6
Global optJumble=0 ;1=rosters, 2=stats
;preferences
Global optMenuRes=2,optWindow=0
Global optMusicVolume=75
Global optFog=0,optShadows=0
Global optCutscenes=1 ;0=only when necessary, 1=hardware only, 2=always
Global optEntourage=0 ;1=select few, 2=all
Global optGrid=1 ;0=list boxes, 1=grid of boxes
Global optTime=2 ;1=weeks, 2=months
Global optStartDate=95093
Global optDifficulty=50
Global optMusicLoad=0 ;-1=greatly reduced, 0=reduced, 1=full
Global optAuto=0

;//////////////////////////////////////////////////////
;------------------- PROGRESS -------------------------
;//////////////////////////////////////////////////////
.Progress
;slot status
Global slot,oldSlot,targetSlot
Dim slotActive(3)
Dim slotName$(3)
Dim slotDate(3)
Dim slotPreview(3)
slotActive(0)=1
;career status
Global gamInherit
Global game,gamChar,gamOffice=1
Global gamDate,gamOldDate,gamNight
Global gamMission,gamTarget,gamDeadline
Dim gamDesk(10) ;-1=broken
Global gamDeskPosition=Rnd(1,6)
;finances
Global gamBank,gamMillionaire
Global gamSales,gamFashion=1
Global gamTax=25,gamRent=1000
Global gamShopping,gamRepairCost=1000
Dim gamFinances(10,2)
;sub-games
Global no_subgames=8
Global gamDevelop,gamWorker
Dim gamScore(10),gamNewScore(10)
Global gamLimit,gamMaxScore,gamNewCredit
Global gamSkill,gamIncrease,gamPerfect
Dim gamHiScore(20),gamHiCredit(20)
;1=locked into project
Dim gamAgreement(50)
;projects
Global no_projects,gamProject,gamRelease
Global gamHardware=1,gamNewHardware
Dim projTitle$(100)
Dim projIdentity(100)
Dim projIdentified(100)
Dim projOldIdentity(100)
Dim projPossibleID(3)
Dim projPossibleBox(3)
Dim projBox(100)
Dim projScreen(100)
Dim projFormat(100) ;0=Online, 1=CD, 2=Floppy, 3=Tape, 4=UMD, 5=Cartridge
Dim projLabel(100)
Dim projReleased(100)
Dim projDate(100)
Dim projReview(100)
Dim projSales(100)
Dim projPirated(100)
Dim projGameplay(100)
Dim projReplay(100)
Dim proj2D(100)
Dim proj3D(100)
Dim projSound(100)
Dim projMusic(100)
Dim projOldGameplay(100)
Dim projOldReplay(100)
Dim projOld2D(100)
Dim projOld3D(100)
Dim projOldSound(100)
Dim projOldMusic(100)
Dim projNewGameplay(100)
Dim projNewReplay(100)
Dim projNew2D(100)
Dim projNew3D(100)
Dim projNewSound(100)
Dim projNewMusic(100)
Dim projHiIncrease(100,10)
Dim projCredit(100,10)
Dim projLocked(100,10)
;charts
Dim chartList(20)
Dim chartOldList(20)
Dim chartGame(50)
Dim chartBox(50)
Dim chartFormat(50)
Dim chartCompany(50)
Dim chartChar(50)
Dim chartPromotion#(50)
Dim chartSales(50)
Dim chartLimited(50)
Dim chartRank(50)
Dim chartOldRank(50)
;chart timers
Dim chartTim(20)
chartTim(10)=20
chartTim(9)=40
chartTim(8)=60
chartTim(7)=80
chartTim(6)=100
chartTim(5)=120
chartTim(4)=140
chartTim(3)=165
chartTim(2)=195
chartTim(1)=230
;hall of fame
Global hiInduct
Dim hiRank(10)
Dim hiChar(10),hiName$(10)
Dim hiPhoto(10),hiPhotoHeight#(10)
Dim hiPhotoR(10),hiPhotoG(10),hiPhotoB(10)
Dim hiWealth(10)
Dim hiPopularity(10)
Dim hiRep(10)
Dim hiExperience(10)
Dim hiFed(10)
;negotiations
Global negSetting
Global negChar,negStar,negVisitor
Global negVariable,negEnemy,negFormat
Global negTopic,negTim,negStage,negSubStage
Global negWorth,negWorthStretch
Global negChunk,negChunkStretch
Global negChances,negVerdict,negInterest
Global negPayOff,negInitialPayOff,negPayOffPrefer
Global negSalary,negInitialSalary
Global negRoyalties,negInitialRoyalties
Global negContract,negInitialContract
Global negPayTotal,negPayRequired
Global negBuyOut,negTeamMultiple
;news events
Global no_events
Dim eEvent(100)
Dim eStats(100) ;1=project, 2=hardware, 3=character
Dim eSubject(100)
Dim eRef(100)
Dim eReaction(100)
Dim eUpdated(100)
Dim eNewspaper(100)
Dim eScene(100,2)
Dim eAdvert(100)
;reviews
Dim revScore(6)
Global revRouteA,revRouteB,revRouteC,revRouteD,revRouteE
Global revBestFeature
;promos
Global promo,speaker
Global promoTopic,promoIntro,promoOutro
Global promoTim,promoWithdraw 
Dim promoReact(10)
Global lineA$,lineB$
Global greenLineA$,greenLineB$
Global redLineA$,redLineB$

;//////////////////////////////////////////////////////
;------------------- ROSTERS --------------------------
;//////////////////////////////////////////////////////
.Rosters
Global fed=1,no_feds=9
Dim fedName$(10)
Dim fedBooker(10)
Dim fedBank(10)
Dim fedRoster(10,50)
Dim fedRank(10,optCharLim)
Dim fedSize(10)
Dim fedPopularity(10)
Dim fedReputation(10)
Dim fedOldPopularity(10)
Dim fedOldReputation(10)
Dim fedNewPopularity(10)
Dim fedNewReputation(10)
Dim fedLocked(10)
;1=exclusivity, 2=generous advance, 3=royalties, 4=long-term, 5=quality, 6=raiders, 7=releasing often
Global no_policies=9
Dim fedPolicy(10)
Dim fedBrand$(10)
;peer rankings
Dim fedList(10)
Dim fedOldList(10)
Dim fedRanked(10)
Dim fedOldRanked(10)
;default stats
fedName$(1)="Nintendo" : fedName$(2)="Sega" : fedName$(3)="Atari"
fedName$(4)="Capcom" : fedName$(5)="Konami" : fedName$(6)="Electronic Arts"
fedName$(7)="Sony" : fedName$(8)="Microsoft" : fedName$(9)="Bedroom Coders"
;office decor
Dim fedWalls(10)
Dim fedCeiling(10)
Dim fedCarpet(10)
n=1 : fedWalls(n)=5 : fedCarpet(n)=3
n=2 : fedWalls(n)=0 : fedCarpet(n)=7
n=3 : fedWalls(n)=6 : fedCarpet(n)=4
n=4 : fedWalls(n)=8 : fedCarpet(n)=3
n=5 : fedWalls(n)=4 : fedCarpet(n)=10
n=6 : fedWalls(n)=1 : fedCarpet(n)=8
n=7 : fedWalls(n)=0 : fedCarpet(n)=0
n=8 : fedWalls(n)=7 : fedCarpet(n)=6
n=9 : fedWalls(n)=3 : fedCarpet(n)=1

;//////////////////////////////////////////////////////
;-------------------- PLAYERS -------------------------
;//////////////////////////////////////////////////////
.Players
;orientation
Global no_plays
Dim p(20)
Dim pFoc(20)
Dim pX#(20)
Dim pY#(20)
Dim pZ#(20)
Dim pA#(20)
;animation
Dim pSeq(20,1100)
Dim pState(20)
Dim pOldState(20)
Dim pAnim(20)
Dim pOldAnim(20)
Dim pAnimTim(20)
Dim pAnimSpeed#(20)
;appearance
Dim pChar(20)
Dim pDesk(20)
Dim pLimb(20,50)
Dim pEyes(20)
Dim pOldEyes(20)
Dim pMouth(20)
Dim pSpeaking(20)
Dim pShadow(20,50)

;///////////////////////////////////////////////////////
;--------------------- CHARACTERS ----------------------
;///////////////////////////////////////////////////////
.Characters
Global no_chars=150
;profile
Dim charPhoto(optCharLim)
Dim charPhotoR(optCharLim)
Dim charPhotoG(optCharLim)
Dim charPhotoB(optCharLim)
Dim charName$(optCharLim)
Dim charTeam(optCharLim)
Dim charTeamName$(optCharLim)
Dim charRoster(optCharLim,10)
Dim charRelationship(optCharLim,optCharLim) ;-=enemy, +=friend
Dim charTradeReaction(optCharLim) ;-=negative, +=positive
Dim charNegotiated(optCharLim)
Dim charHappiness(optCharLim)
Dim charWorked(optCharLim)
Dim charDedicated(optCharLim)
;attributes
Dim charDesign(optCharLim)
Dim charProgram(optCharLim)
Dim char2D(optCharLim)
Dim char3D(optCharLim)
Dim charSound(optCharLim)
Dim charMusic(optCharLim)
Dim charRep(optCharLim)
Dim charOldDesign(optCharLim)
Dim charOldProgram(optCharLim)
Dim charOld2D(optCharLim)
Dim charOld3D(optCharLim)
Dim charOldSound(optCharLim)
Dim charOldMusic(optCharLim)
Dim charOldRep(optCharLim)
Dim charNewDesign(optCharLim)
Dim charNewProgram(optCharLim)
Dim charNew2D(optCharLim)
Dim charNew3D(optCharLim)
Dim charNewSound(optCharLim)
Dim charNewMusic(optCharLim)
Dim charNewRep(optCharLim)
;build
Dim charAge(optCharLim)
Dim charHeight(optCharLim)
Dim charBuild(optCharLim)
Dim charEyes(optCharLim)
Dim charHatStyle(optCharLim)
Dim charHat(optCharLim)
Dim charSpecs(optCharLim)
Dim charAccessory(optCharLim)
;clothing
Dim charHairStyle(optCharLim)
Dim charHair(optCharLim)
Dim charFace(optCharLim)
Dim charBeard(optCharLim)
Dim charBody(optCharLim)
Dim charArms(optCharLim)
Dim charHands(optCharLim)
Dim charShorts(optCharLim)
Dim charLegs(optCharLim)
Dim charShoes(optCharLim)
Dim charBaggy(optCharLim) ;0=tight, 1=baggy top, 2=baggy pants, 3=all baggy
;career status
Dim charFed(optCharLim)
Dim charExperience(optCharLim,9) ;0=player's band, 1+=label
Dim charWorth(optCharLim)
Dim charContract(optCharLim)
Dim charSalary(optCharLim)
Dim charRoyalties(optCharLim)
Dim charVacant(optCharLim)
Dim charOldVacant(optCharLim)
;1=tolerated debt, 2=recently let go
Dim charAgreement(optCharLim,50)
;handles
Dim charEvent(optCharLim)
Dim charRanked(optCharLim)
Dim charUpdated(optCharLim)
;rankings
Dim charRanked(optCharLim)
Dim charLeader(10),charOldLeader(10)
;editor
Global page,no_pages,no_boxes
Dim pageList(10)
Global editChar,editFed,editFoc,editModel
Global editScroll,editFilter=9,editPoints
Global editExamine,editClipboard
Dim editPointsY(10)
Global blockData,blockName,blockDemeanour
Global blockHair,blockCostume,blockContract
Global advice$,adviceX,adviceY
Global warning$,warnX,warnY
Global no_accessories=7,no_hatstyles=9,no_specs=8
Global no_models=7,no_hairstyles=47
Global hatX#,hatY#,hatZ#
Global hatXA#,hatYA#,hatZA#
;stat pursuits
Dim statLevel(6)
statLevel(1)=30
statLevel(2)=50
statLevel(3)=70
statLevel(4)=85
statLevel(5)=100
statLevel(6)=100

;///////////////////////////////////////////////////////
;----------------------- WORLD -------------------------
;///////////////////////////////////////////////////////
.World
Global world
Global wNorth#,wSouth#,wEast#,wWest#,wGround#,wCeiling#
Global meetX#,meetY#,meetZ#,meetA#
;workstations
Global no_desks,no_plants=4
Dim desk(20)
Dim deskHardware(20)
Dim deskX#(20)
Dim deskY#(20)
Dim deskZ#(20)
Dim deskA#(20)
Dim deskPlant(20)
Dim deskPoster(20)
Dim deskGame(20,2)
For cyc=1 To 12
 deskPlant(cyc)=Rnd(1,no_plants)
Next
;videos
Dim videoScreen(10)
Dim videoOldScreen(10)
;camera 
Global cam,camPivot,dummy
Global camType,camFoc
Global camX#,camY#=75,camZ#
Global camTX#,camTY#,camTZ#
Global camOldX#,camOldY#,camOldZ#
Global camPivX#,camPivY#=100,camPivZ#
Global camPivTX#,camPivTY#,camPivTZ#
Global camAccel=100,camPivAccel=100
Global camMouseX#,camMouseY#
;smooth co-ordination
Global speedX#,speedY#,speedZ#
Global centreX#,centreY#,centreZ#
;lighting
Global light,lightPivot
Global lightR#,lightG#,lightB#
Global lightTR#,lightTG#,lightTB#
Global ambR#=100,ambG#=100,ambB#=100
Global ambTR#,ambTG#,ambTB#
Global atmosR#=100,atmosG#=100,atmosB#=100
Global atmosTR#,atmosTG#,atmosTB#

;///////////////////////////////////////////////////////
;----------------------- MUSIC -------------------------
;///////////////////////////////////////////////////////
.Music
;music
Global chTheme,chCurrentTheme,chThemeChange
Global chThemeVol#,chThemeTarget#
Dim sTheme(100)
;count files
Global no_themes,no_songs=0
folder=ReadDir(CurrentDir$()+"Music/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,5))
  If FileType(CurrentDir$()+"Music/"+filename$)=1 And namer$="THEME" And no_songs<100 Then no_songs=no_songs+1
 Until filename$=""
CloseDir(folder)
;loading function
Function LoadMusic()
 Loader("Please Wait","Loading Music")
 no_themes=no_songs
 If optMusicLoad=0 Then no_themes=no_themes/2
 If optMusicLoad=-1 Then no_themes=no_themes/3
 For cyc=1 To no_themes
  sTheme(cyc)=0
 Next
 For cyc=1 To no_themes
  Loader("","")
  If optMusicLoad>0
   sTheme(cyc)=LoadSound("Music/Theme"+Dig$(cyc,10)+".mp3")
  Else
   Repeat
    sTheme(cyc)=LoadSound("Music/Theme"+Dig$(Rnd(1,no_songs),10)+".mp3") : conflict=0
    For v=1 To no_themes
     If cyc<>v And sTheme(cyc)=sTheme(v) Then conflict=1
    Next
   Until conflict=0
  EndIf
 Next
 ;chThemeChange=Rnd(1,no_themes)
 ;LoopSound sTheme(chThemeChange)
 ;chTheme=PlaySound(sTheme(chThemeChange))
 ;chCurrentTheme=chThemeChange : chThemeChange=0
 ;chThemeVol#=PercentOf#(1.0,optMusicVolume)
End Function

;///////////////////////////////////////////////////////
;----------------------- SOUND -------------------------
;///////////////////////////////////////////////////////
.Sound
;menu effects
Global sMenuBrowse=LoadSound("Sound/Browse.wav")
Global sMenuSelect=LoadSound("Sound/Select.wav")
Global sMenuGo=LoadSound("Sound/Confirm.wav")
Global sMenuBack=LoadSound("Sound/Cancel.wav")
Global sVoid=LoadSound("Sound/Void.wav")
Global sCorrect=LoadSound("Sound/Correct.wav")
Global sIncorrect=LoadSound("Sound/Incorrect.wav")
Global sFail=LoadSound("Sound/Fail.wav")
Global sMilestone=LoadSound("Sound/Milestone.wav")
Global sCheer=LoadSound("Sound/Cheer.wav")
Global sGrumble=LoadSound("Sound/Grumble.wav")
Global sCamera=LoadSound("Sound/Camera.wav")
Global sPaper=LoadSound("Sound/Paper.wav")
Global sCash=LoadSound("Sound/Cash.wav")
Global sTrash=LoadSound("Sound/Trash.wav")
Global sProduce=LoadSound("Sound/Produce.wav")
Global sTyping=LoadSound("Sound/Typing.wav")
;game effects
Global sSwing=LoadSound("Sound/Swing.wav")
Global sFire=LoadSound("Sound/Games/Fire.wav")
Global sExplode=LoadSound("Sound/Games/Explode.wav")
Global sInvader=LoadSound("Sound/Games/Invader.wav")
Global sBall=LoadSound("Sound/Games/Ball.wav")
Global sCollect=LoadSound("Sound/Games/Collect.wav")
Global sGong=LoadSound("Sound/Games/Gong.wav")
;sure shot
Dim sShot(2)
sShot(1)=LoadSound("Sound/Games/Shot01.wav")
sShot(2)=LoadSound("Sound/Games/Shot02.wav")
Global sReload=LoadSound("Sound/Games/Reload.wav")
Global sRicochet=LoadSound("Sound/Games/Ricochet.wav")
Global sWound=LoadSound("Sound/Games/Wound.wav")
Global sPain=LoadSound("Sound/Games/Pain.wav")
Global sDie=LoadSound("Sound/Games/Die.wav")

;///////////////////////////////////////////////////////
;--------------------- GRAPHICS ------------------------
;///////////////////////////////////////////////////////
.Images
;variables
Dim gLogo(3)
Dim gMenu(20)
Dim gScroll(4)
Dim gStat(20)
Dim gLabel(10)
Dim gPopRating(5),gArtRating(5)
Dim gSmiley(5)
Dim gRoyalties(5)
Dim gMonth(30)
Dim gEvent(10)
Dim gVenue(30)
Dim gIconSmall(10)
Dim gIconLarge(10)
Dim gMeter(5)
Dim gVertMeter(5)
Global gBackground,gPrompt
Global gMDickie,gOutro
Global gCursor,gCrown
Global gPiracy,gRepair
Global gAbsent,gContract
Global gCalendar,gMoney
Global gMagazine,gReport
Global gChartNew,gChartStatic
Global gChartUp,gChartDown
Dim gNewsIdentity(5)
Dim gNewsScene(5)
Dim gNewsAdvert(20)
Dim gHardware(100)
Dim gFormat(5)
Dim gDesk(2)
;subgames
Dim gControl(3)
Global gExplode,gSpaceship,gBullet
Global gWell,gWellBG
Global gAstroBullet
Global gGuru
Global gBall,gFrame
Dim gBat(3)
Global gSnakeFrame
Dim gSnake(5)
Dim gSoldier(2),gSureBullet(2)
Global gCrosshair

;LOADING PROCESS
Function LoadImages() 
 ;background
 Loader("Please Wait","Loading Images")
 gBackground=LoadImage("Graphics/Background.png")
 If GraphicsWidth()<>800 And GraphicsHeight()<>600 Then ResizeImage gBackground,GraphicsWidth(),GraphicsHeight()
 MaskImage gBackground,255,0,255
 gPrompt=LoadImage("Graphics/Prompt.png")
 MaskImage gPrompt,255,0,255
 ;logos
 For count=1 To 2
  gLogo(count)=LoadImage("Graphics/Logo"+Dig$(count,10)+".png")
  MaskImage gLogo(count),255,0,255
 Next
 gMDickie=LoadImage("Graphics/MDickie.png")
 MaskImage gMDickie,10,10,10
 gOutro=LoadImage("Graphics/Outro.JPG")
 MaskImage gOutro,255,0,255
 ;menu boxes
 For count=1 To 6
  gMenu(count)=LoadImage("Graphics/Menus/Menu"+Dig$(count,10)+".png")
  MaskImage gMenu(count),255,0,255
 Next
 gCursor=LoadImage("Graphics/Cursor.png")
 MaskImage gCursor,255,0,255
 ;scrollers
 For count=1 To 4
  gScroll(count)=LoadImage("Graphics/Menus/Scroll"+Dig$(count,10)+".png")
  MaskImage gScroll(count),255,0,255
 Next
 ;main icons
 ;Loader("","")
 For count=1 To 8
  gIconSmall(count)=LoadImage("Graphics/Icons/Small/Icon"+Dig$(count,10)+".png")
  MaskImage gIconSmall(count),255,0,255
  gIconLarge(count)=LoadImage("Graphics/Icons/Large/Icon"+Dig$(count,10)+".png")
  MaskImage gIconLarge(count),255,0,255
 Next
 For count=1 To 3
  gSmiley(count)=LoadImage("Graphics/Icons/Smiley"+Dig$(count,10)+".png")
  MaskImage gSmiley(count),255,0,255
 Next 
 gContract=LoadImage("Graphics/Icons/Contract.png")
 MaskImage gContract,255,0,255
 gCalendar=LoadImage("Graphics/Icons/Calendar.png")
 MaskImage gCalendar,255,0,255
 gMoney=LoadImage("Graphics/Icons/Money.png")
 MaskImage gMoney,255,0,255
 gCrown=LoadImage("Graphics/Icons/Crown.png")
 MaskImage gCrown,255,0,255
 ;meters
 For count=0 To 3
  gMeter(count)=LoadImage("Graphics/Meters/Meter"+Dig$(count,10)+".png")
  MaskImage gMeter(count),255,0,255
  gVertMeter(count)=LoadImage("Graphics/Meters/Vertical"+Dig$(count,10)+".png")
  MaskImage gVertMeter(count),255,0,255
 Next
 ;chart
 ;Loader("","")
 gChartNew=LoadImage("Graphics/Icons/New.png")
 MaskImage gChartNew,255,0,255
 gChartStatic=LoadImage("Graphics/Icons/Static.png")
 MaskImage gChartStatic,255,0,255
 gChartUp=LoadImage("Graphics/Icons/Up.png")
 MaskImage gChartUp,255,0,255
 gChartDown=LoadImage("Graphics/Icons/Down.png")
 MaskImage gChartDown,255,0,255
 ;articles
 gMagazine=LoadImage("Graphics/Articles/Magazine.png")
 MaskImage gMagazine,255,0,255
 gReport=LoadImage("Graphics/Articles/Report.png")
 MaskImage gReport,255,0,255
 ;newspaper detail
 ;Loader("","")
 For count=1 To 1
  gNewsIdentity(count)=LoadImage("Graphics/Articles/Identity"+Dig$(count,10)+".png")
  MaskImage gNewsIdentity(count),255,0,255
 Next
 For count=1 To 3
  gNewsScene(count)=LoadImage("Graphics/Articles/Scene"+Dig$(count,10)+".JPG")
  MaskImage gNewsScene(count),255,0,255
 Next
 For count=1 To 14
  gNewsAdvert(count)=LoadImage("Graphics/Articles/Advert"+Dig$(count,10)+".JPG")
  MaskImage gNewsAdvert(count),255,0,255
 Next
 ;workstations
 For count=0 To 1
  gDesk(count)=LoadImage("Graphics/Desk"+Dig$(count,10)+".png")
  MaskImage gDesk(count),255,0,255
 Next
 gPiracy=LoadImage("Graphics/Icons/Piracy.png")
 MaskImage gPiracy,255,0,255
 gRepair=LoadImage("Graphics/Icons/Repair.png")
 MaskImage gRepair,255,0,255
 gAbsent=LoadImage("Graphics/Absent.png")
 MaskImage gAbsent,255,0,255
 ;companies
 ;Loader("","")
 For count=1 To 9
  gLabel(count)=LoadImage("Graphics/Companies/Company"+Dig$(count,10)+".png")
  MaskImage gLabel(count),10,10,10
 Next
 ;hardware
 Loader("","")
 For count=1 To no_machines
  gHardware(count)=LoadImage("Graphics/Hardware/Machine"+Dig$(count,10)+".jpg")
  If gHardware(count)=0 Then gHardware(count)=LoadImage("Graphics/Hardware/Machine01.jpg")  
  MaskImage gHardware(count),255,0,255
 Next
  For count=0 To 5
  gFormat(count)=LoadImage("Graphics/Hardware/Formats/Format"+Dig$(count,10)+".png")
  MaskImage gFormat(count),255,0,255
 Next
 ;SUB-GAMES
 For count=1 To 3
  gControl(count)=LoadImage("Graphics/Sub-Games/Control"+Dig$(count,10)+".png")
  MaskImage gControl(count),255,0,255
 Next
 gCrosshair=LoadImage("Graphics/Sub-Games/Crosshair.png")
 MaskImage gCrosshair,255,0,255
 ;space invaders
 gExplode=LoadAnimImage("Graphics/Sub-Games/Explode.png",36,36,0,4)
 MaskImage gExplode,255,0,255
 gBullet=LoadImage("Graphics/Sub-Games/Space Invaders/Bullet.png")
 MaskImage gBullet,255,0,255
 gSpaceship=LoadImage("Graphics/Sub-Games/Space Invaders/Spaceship.png")
 MaskImage gSpaceship,255,0,255 
 ;tetris
 gWell=LoadImage("Graphics/Sub-Games/Tetris/Well.png")
 MaskImage gWell,255,0,255
 gWellBG=LoadImage("Graphics/Sub-Games/Tetris/Background.png")
 MaskImage gWellBG,255,0,255
 ;break-out
 For count=1 To 3
  gBat(count)=LoadImage("Graphics/Sub-Games/Breakout/Bat"+Dig$(count,10)+".png")
  MaskImage gBat(count),255,0,255
 Next
 gBall=LoadImage("Graphics/Sub-Games/Breakout/Ball.png")
 MaskImage gBall,255,0,255
 gFrame=LoadImage("Graphics/Sub-Games/Breakout/Frame.png")
 MaskImage gFrame,255,0,255 
 ;asteroids
 gAstroBullet=LoadImage("Graphics/Sub-Games/Asteroids/Bullet.png")
 MaskImage gAstroBullet,255,0,255
 ;moksha
 gGuru=LoadAnimImage("Graphics/Sub-Games/Moksha/Guru.png",151,143,0,10)
 MaskImage gGuru,255,0,255
 ;snake
 For count=1 To 5
  gSnake(count)=LoadImage("Graphics/Sub-Games/Snake/Snake"+Dig$(count,10)+".png")
  MaskImage gSnake(count),255,0,255
 Next
 gSnakeFrame=LoadImage("Graphics/Sub-Games/Snake/Frame.png")
 MaskImage gSnakeFrame,255,0,255
 ;sure shot
 For count=1 To 2
  gSoldier(count)=LoadAnimImage("Graphics/Sub-Games/Sure Shot/Soldier"+Dig$(count,10)+".png",120,120,0,12)
  MaskImage gSoldier(count),255,0,255
  gSureBullet(count)=LoadImage("Graphics/Sub-Games/Sure Shot/Bullet"+Dig$(count,10)+".png")
  MaskImage gSureBullet(count),255,0,255
 Next
End Function

;///////////////////////////////////////////////////////
;--------------------- TEXTURES ------------------------
;///////////////////////////////////////////////////////
.Textures
;count hat textures
Global no_hats=0
folder=ReadDir(CurrentDir$()+"Characters/Headwear/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,3))
  If FileType(CurrentDir$()+"Characters/Headwear/"+filename$)=1 And namer$="HAT" Then no_hats=no_hats+1
 Until filename$=""
CloseDir(folder)
;count hair textures
Global no_hairs=0
folder=ReadDir(CurrentDir$()+"Characters/Hair/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,4))
  If FileType(CurrentDir$()+"Characters/Hair/"+filename$)=1 And namer$="HAIR" Then no_hairs=no_hairs+1
 Until filename$=""
CloseDir(folder)
;count beard textures
Global no_beards=0
folder=ReadDir(CurrentDir$()+"Characters/Beards/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,5))
  If FileType(CurrentDir$()+"Characters/Beards/"+filename$)=1 And namer$="BEARD" Then no_beards=no_beards+1
 Until filename$=""
CloseDir(folder)
;count face textures
Global no_faces=0
folder=ReadDir(CurrentDir$()+"Characters/Faces/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,4))
  If FileType(CurrentDir$()+"Characters/Faces/"+filename$)=1 And namer$="FACE" Then no_faces=no_faces+1
 Until filename$=""
CloseDir(folder)
;count body textures
Global no_bodies=0
folder=ReadDir(CurrentDir$()+"Characters/Bodies/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,4))
  If FileType(CurrentDir$()+"Characters/Bodies/"+filename$)=1 And namer$="BODY" Then no_bodies=no_bodies+1
 Until filename$=""
CloseDir(folder)
;count arm textures
Global no_arms=0
folder=ReadDir(CurrentDir$()+"Characters/Arms/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,3))
  If FileType(CurrentDir$()+"Characters/Arms/"+filename$)=1 And namer$="ARM" Then no_arms=no_arms+1
 Until filename$=""
CloseDir(folder)
;count hand textures
Global no_hands=0
folder=ReadDir(CurrentDir$()+"Characters/Hands/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,5))
  If FileType(CurrentDir$()+"Characters/Hands/"+filename$)=1 And namer$="HANDS" Then no_hands=no_hands+1
 Until filename$=""
CloseDir(folder)
;count leg textures
Global no_legs=0
folder=ReadDir(CurrentDir$()+"Characters/Legs/")
 Repeat
  filename$=NextFile$(folder)
  namer$=Upper$(Left$(filename$,4))
  If FileType(CurrentDir$()+"Characters/Legs/"+filename$)=1 And namer$="LEGS" Then no_legs=no_legs+1
 Until filename$=""
CloseDir(folder)
;world variables
Dim tPlant(10)
Dim tDevelop(6)
Global tScreenshot,tBoxArt
;costume variables
Global tEars,tPony
Dim tEyes(3)
Dim tMouth(5)
Dim tSpecs(3)
Dim tHat(no_hats),tHatAccess(no_hats)
Dim tShaved(10)
Dim tBeard(no_beards)
Dim tHair(no_hairs),tHairAccess(no_hairs)
Dim tFace(no_faces),tFaceType(no_faces),tFaceAccess(no_faces)
Dim tBody(no_bodies),tBodyType(no_bodies),tBodyAccess(no_bodies)
Dim tArm(no_arms),tArmType(no_arms),tArmAccess(no_arms)
Dim tHands(no_hands),tHandType(no_hands),tHandsAccess(no_hands)
Dim tLegs(no_legs),tLegType(no_legs),tLegsAccess(no_legs)
Dim tBodyShade(100),tBodyShadeAccess(100)
Dim tArmShade(100),tArmShadeAccess(100)
Dim tHandShade(100),tHandShadeAccess(100)
Dim tLegShade(100),tLegShadeAccess(100)
;LOADING PROCESS
Function LoadTextures()
 Loader("Please Wait","Loading Textures")
 ;foliage
 For count=1 To no_plants
  tPlant(count)=LoadTexture("World/Foliage/Flower"+Dig$(count,10)+".png",4)
 Next
 ;development screens
 For count=1 To 6
  tDevelop(count)=LoadTexture("Games/Development/Screen"+Dig$(count,10)+".JPG")
 Next
 ;facial expressions
 Loader("Please Wait","Loading Expressions")
 tEars=LoadTexture("Characters/Expressions/Ears.JPG")
 For count=1 To 3
  tEyes(count)=LoadTexture("Characters/Expressions/Eyes0"+count+".JPG")
 Next
 For count=0 To 5
  tMouth(count)=LoadTexture("Characters/Expressions/Mouth0"+count+".JPG")
 Next
 ;costume variations
 Loader("Please Wait","Loading Accessories") 
 For count=1 To no_hats
  tHat(count)=LoadTexture("Characters/Headwear/Hat"+Dig$(count,10)+".JPG")
 Next
 For count=1 To 3
  tSpecs(count)=LoadTexture("Characters/Accessories/Specs"+Dig$(count,10)+".JPG")
 Next
 tPony=LoadTexture("Characters/Hair/Pony.JPG") 
 For count=1 To 6
  tShaved(count)=LoadTexture("Characters/Hair/Shaved"+Dig$(count,10)+".JPG")
 Next
 For count=1 To no_beards
  tBeard(count)=LoadTexture("Characters/Beards/Beard"+Dig$(count,10)+".JPG")
 Next
 For count=1 To no_hairs
  tHair(count)=LoadTexture("Characters/Hair/Hair"+Dig$(count,10)+".JPG")
 Next
 Loader("Please Wait","Loading Faces")
 For count=1 To no_faces
  ;Loader("","")
  tFaceType(count)=0
  For scan=1 To 3
   If FileType("Characters/Faces/Face"+Dig$(count,10)+" ("+scan+").JPG")=1 Then tFaceType(count)=scan
  Next
  tFace(count)=LoadTexture("Characters/Faces/Face"+Dig$(count,10)+""+TextureAffix$(tFaceType(count))+".JPG")
 Next
 Loader("Please Wait","Loading Bodies")
 For count=1 To no_bodies
  ;Loader("","");Loader("Please Wait","Loading Body "+Dig$(count,10)+" of "+no_bodies)
  tBodyType(count)=0
  For scan=1 To 100
   If FileType("Characters/Bodies/Body"+Dig$(count,10)+" ("+scan+").JPG")=1 Then tBodyType(count)=scan
  Next
  tBody(count)=LoadTexture("Characters/Bodies/Body"+Dig$(count,10)+""+TextureAffix$(tBodyType(count))+".JPG")
 Next
 Loader("Please Wait","Loading Arms")
 For count=1 To no_arms
  ;Loader("","");Loader("Please Wait","Loading Arm "+Dig$(count,10)+" of "+no_arms)
  tArmType(count)=0
  For scan=1 To 100
   If FileType("Characters/Arms/Arm"+Dig$(count,10)+" ("+scan+").JPG")=1 Then tArmType(count)=scan
  Next
  tArm(count)=LoadTexture("Characters/Arms/Arm"+Dig$(count,10)+""+TextureAffix$(tArmType(count))+".JPG")
 Next
 Loader("Please Wait","Loading Hands")
 For count=1 To no_hands
  tHandType(count)=0
  For scan=1 To 100
   If FileType("Characters/Hands/Hands"+Dig$(count,10)+" ("+scan+").JPG")=1 Then tHandType(count)=scan
  Next
  tHands(count)=LoadTexture("Characters/Hands/Hands"+Dig$(count,10)+""+TextureAffix$(tHandType(count))+".JPG")
 Next 
 Loader("Please Wait","Loading Legs")
 For count=1 To no_legs
  ;Loader("","");Loader("Please Wait","Loading Legs "+Dig$(count,10)+" of "+no_legs)
  tLegType(count)=0
  For scan=1 To 100
   If FileType("Characters/Legs/Legs"+Dig$(count,10)+" ("+scan+").JPG")=1 Then tLegType(count)=scan
  Next
  tLegs(count)=LoadTexture("Characters/Legs/Legs"+Dig$(count,10)+""+TextureAffix$(tLegType(count))+".JPG")
 Next
 ;racial shades
 Loader("Please Wait","Loading Shades")
 For count=1 To 100
  ;If FileType(CurrentDir$()+"Characters/Bodies/Shading/Body"+Dig$(count,10)+".JPG")=1
   ;tBodyShade(count)=LoadTexture("Characters/Bodies/Shading/Body"+Dig$(count,10)+".JPG") 
  ;Else
   ;tBodyShade(count)=LoadTexture("Characters/Bodies/Shading/Body"+Dig$(count,10)+".png") 
  ;EndIf
  tBodyShade(count)=LoadTexture("Characters/Bodies/Shading/Body"+Dig$(count,10)+".png") 
  tArmShade(count)=LoadTexture("Characters/Arms/Shading/Arm"+Dig$(count,10)+".png")
  tHandShade(count)=LoadTexture("Characters/Hands/Shading/Hands"+Dig$(count,10)+".JPG")
  tLegShade(count)=LoadTexture("Characters/Legs/Shading/Legs"+Dig$(count,10)+".png") 
 Next
End Function

;RESTORE TEXTURES
Function RestoreTextures()
 For count=1 To no_hats
  If tHat(count)>0 And tHatAccess(count)>0
   FreeTexture tHat(count)
   tHat(count)=LoadTexture("Characters/Headwear/Hat"+Dig$(count,10)+".JPG")
  EndIf
 Next
 For count=1 To no_hairs
  If tHair(count)>0 And tHairAccess(count)>0
   FreeTexture tHair(count)
   tHair(count)=LoadTexture("Characters/Hair/Hair"+Dig$(count,10)+".JPG")
  EndIf
 Next
 For count=1 To no_faces
  If tFace(count)>0 And tFaceAccess(count)>0
   FreeTexture tFace(count)
   tFace(count)=LoadTexture("Characters/Faces/Face"+Dig$(count,10)+""+TextureAffix$(tFaceType(count))+".JPG")
  EndIf
 Next
 For count=1 To no_bodies
  If tBody(count)>0 And tBodyAccess(count)>0
   FreeTexture tBody(count)
   tBody(count)=LoadTexture("Characters/Bodies/Body"+Dig$(count,10)+""+TextureAffix$(tBodyType(count))+".JPG")
  EndIf
 Next
 For count=1 To no_arms
  If tArm(count)>0 And tArmAccess(count)>0
   FreeTexture tArm(count)
   tArm(count)=LoadTexture("Characters/Arms/Arm"+Dig$(count,10)+""+TextureAffix$(tArmType(count))+".JPG")
  EndIf
 Next
 For count=1 To no_hands
  If tHands(count)>0 And tHandsAccess(count)>0
   FreeTexture tHands(count)
   tHands(count)=LoadTexture("Characters/Hands/Hands"+Dig$(count,10)+""+TextureAffix$(tHandType(count))+".JPG")
  EndIf
 Next
 For count=1 To no_legs
  If tLegs(count)>0 And tLegsAccess(count)>0
   FreeTexture tLegs(count)
   tLegs(count)=LoadTexture("Characters/Legs/Legs"+Dig$(count,10)+""+TextureAffix$(tLegType(count))+".JPG")
  EndIf
 Next
 For count=1 To 100
  If tBodyShade(count)>0 And tBodyShadeAccess(count)>0
   FreeTexture tBodyShade(count)
   tBodyShade(count)=LoadTexture("Characters/Bodies/Shading/Body"+Dig$(count,10)+".png") 
  EndIf
  If tArmShade(count)>0 And tArmShadeAccess(count)>0
   FreeTexture tArmShade(count)
   tArmShade(count)=LoadTexture("Characters/Arms/Shading/Arm"+Dig$(count,10)+".png") 
  EndIf
  If tHandShade(count)>0 And tHandShadeAccess(count)>0
   FreeTexture tHandShade(count)
   tHandShade(count)=LoadTexture("Characters/Hands/Shading/Hands"+Dig$(count,10)+".JPG") 
  EndIf
  If tLegShade(count)>0 And tLegShadeAccess(count)>0
   FreeTexture tLegShade(count)
   tLegShade(count)=LoadTexture("Characters/Legs/Shading/Legs"+Dig$(count,10)+".png") 
  EndIf
 Next
End Function

;GET TEXTURE AFFIX
Function TextureAffix$(mask)
 affix$=""
 If mask>0 Then affix$=" ("+mask+")"
 Return affix$ 
End Function

;RESET TEXTURE RECORD
Function ResetTextures()
 ;costumes
 For count=1 To no_hats
  tHatAccess(count)=0
 Next
 For count=1 To no_hairs
  tHairAccess(count)=0
 Next
 For count=1 To no_faces
  tFaceAccess(count)=0
 Next
 For count=1 To no_bodies
  tBodyAccess(count)=0
 Next
 For count=1 To no_arms
  tArmAccess(count)=0
 Next
 For count=1 To no_hands
  tHandsAccess(count)=0
 Next
 For count=1 To no_legs
  tLegsAccess(count)=0
 Next
 ;racial shades
 For count=1 To 100
  tBodyShadeAccess(count)=0
  tArmShadeAccess(count)=0
  tHandShadeAccess(count)=0
  tLegShadeAccess(count)=0
 Next
End Function