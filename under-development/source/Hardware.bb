;//////////////////////////////////////////////////////////////////////////////
;------------------------ UNDER DEVELOPMENT: HARDWARE DATA --------------------
;//////////////////////////////////////////////////////////////////////////////

;///////////////////////////////////////////////////////
;--------------------- FORMATS -------------------------
;///////////////////////////////////////////////////////
.Formats
Dim formatName$(5)
Dim formatCost(5)
formatName$(0)="Digital" : formatCost(0)=0
formatName$(1)="Compact Disc" : formatCost(1)=5
formatName$(2)="Floppy Disc" : formatCost(2)=5
formatName$(3)="Cassette" : formatCost(3)=5
formatName$(4)="Media Disc" : formatCost(4)=10
formatName$(5)="Cartridge" : formatCost(5)=10

;///////////////////////////////////////////////////////
;--------------------- HARDWARE ------------------------
;///////////////////////////////////////////////////////
.Hardware
Global no_machines=38
Global hardFoc,hardScroll
Dim hardList(100)
Dim hardListed(100)
Dim hardBrand$(100)
Dim hardTitle$(100)
Dim hardModel$(100) ;name of 3D model
Dim hardCom$(100) ;name of computer used for development
Dim hardTV$(100) ;name of preferred monitor
Dim hardFormat(100) ;0=Online, 1=CD, 2=Floppy, 3=Tape, 4=UMD, 5=Cartridge
Dim hardHandheld(100) ;1=vertical (gameboy), 2=horizontal (PSP), 3=book (DS)
Dim hardCost(100)
Dim hardRetail(100)
Dim hardOwned(100)
Dim hardWeek(100),hardMonth(100),hardYear(100)
Dim hardReleased(100)
Dim hardOldPromotion#(100)
Dim hardPromotion#(100)
Dim hardUsers(100)
Dim hardPeakUsers(100)
Dim hardPirated(100)
Dim hardGameplay(100)
Dim hardReplay(100)
Dim hard2D(100)
Dim hard3D(100)
Dim hardSound(100)
Dim hardMusic(100)
Dim hardOldGameplay(100)
Dim hardOldReplay(100)
Dim hardOld2D(100)
Dim hardOld3D(100)
Dim hardOldSound(100)
Dim hardOldMusic(100)
Dim hardNewGameplay(100)
Dim hardNewReplay(100)
Dim hardNew2D(100)
Dim hardNew3D(100)
Dim hardNewSound(100)
Dim hardNewMusic(100)
Dim hardStandard(7),hardLeader(7) ;industry standard in each category
Global hardOldLeader=1
;default data
n=1 : hardBrand$(n)="Atari" : hardTitle$(n)="Atari 2600"
hardModel$(n)="Atari" : hardCom$(n)="Keyboard" : hardTV$(n)="OldTV"
hardWeek(n)=1 : hardMonth(n)=1 : hardYear(n)=1981
hardCost(n)=0 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=40 : hardReplay(n)=40
hard2D(n)=40 : hard3D(n)=25
hardSound(n)=40 : hardMusic(n)=40
;first computers
n=2 : hardBrand$(n)="Sinclair" : hardTitle$(n)="ZX Spectrum"
hardModel$(n)="Spectrum" : hardCom$(n)="Spectrum" : hardTV$(n)="OldTV"
hardWeek(n)=3 : hardMonth(n)=2 : hardYear(n)=1982
hardCost(n)=25000 : hardRetail(n)=10 : hardFormat(n)=3 : hardHandheld(n)=0
hardGameplay(n)=45 : hardReplay(n)=50
hard2D(n)=45 : hard3D(n)=30
hardSound(n)=45 : hardMusic(n)=45
n=3 : hardBrand$(n)="Commodore" : hardTitle$(n)="Commodore 64"
hardModel$(n)="C64" : hardCom$(n)="C64" : hardTV$(n)="Monitor"
hardWeek(n)=2 : hardMonth(n)=3 : hardYear(n)=1983
hardCost(n)=50000 : hardRetail(n)=10 : hardFormat(n)=3 : hardHandheld(n)=0
hardGameplay(n)=50 : hardReplay(n)=55
hard2D(n)=55 : hard3D(n)=40
hardSound(n)=50 : hardMusic(n)=50
n=4 : hardBrand$(n)="Amstrad" : hardTitle$(n)="Amstrad CPC"
hardModel$(n)="Amstrad" : hardCom$(n)="Amstrad" : hardTV$(n)="BoxTV"
hardWeek(n)=4 : hardMonth(n)=1 : hardYear(n)=1984
hardCost(n)=25000 : hardRetail(n)=10 : hardFormat(n)=3 : hardHandheld(n)=0
hardGameplay(n)=50 : hardReplay(n)=55
hard2D(n)=50 : hard3D(n)=35
hardSound(n)=50 : hardMusic(n)=50
;8-bit era
n=5 : hardBrand$(n)="Nintendo" : hardTitle$(n)="NES"
hardModel$(n)="NES" : hardCom$(n)="Keyboard" : hardTV$(n)="OldTV"
hardWeek(n)=1 : hardMonth(n)=4 : hardYear(n)=1986
hardCost(n)=75000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=60 : hardReplay(n)=60
hard2D(n)=55 : hard3D(n)=35
hardSound(n)=60 : hardMusic(n)=60
n=6 : hardBrand$(n)="Sega" : hardTitle$(n)="Master System"
hardModel$(n)="Master" : hardCom$(n)="Keyboard" : hardTV$(n)="OldTV"
hardWeek(n)=2 : hardMonth(n)=6 : hardYear(n)=1986
hardCost(n)=50000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=55 : hardReplay(n)=55
hard2D(n)=60 : hard3D(n)=40
hardSound(n)=60 : hardMusic(n)=60
;early handhelds
n=7 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Gameboy"
hardModel$(n)="Gameboy" : hardCom$(n)="Keyboard" : hardTV$(n)="OldTV"
hardWeek(n)=1 : hardMonth(n)=5 : hardYear(n)=1989
hardCost(n)=50000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=1
hardGameplay(n)=55 : hardReplay(n)=55
hard2D(n)=45 : hard3D(n)=30
hardSound(n)=55 : hardMusic(n)=55
n=8 : hardBrand$(n)="Sega" : hardTitle$(n)="Game Gear"
hardModel$(n)="Gear" : hardCom$(n)="Keyboard" : hardTV$(n)="OldTV"
hardWeek(n)=2 : hardMonth(n)=3 : hardYear(n)=1990
hardCost(n)=75000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=2
hardGameplay(n)=55 : hardReplay(n)=50
hard2D(n)=60 : hard3D(n)=35
hardSound(n)=50 : hardMusic(n)=50
n=9 : hardBrand$(n)="Atari" : hardTitle$(n)="Lynx"
hardModel$(n)="Lynx" : hardCom$(n)="AtariST" : hardTV$(n)="OldTV"
hardWeek(n)=3 : hardMonth(n)=5 : hardYear(n)=1990
hardCost(n)=75000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=2
hardGameplay(n)=50 : hardReplay(n)=55
hard2D(n)=55 : hard3D(n)=40
hardSound(n)=55 : hardMusic(n)=55
;16-bit computers
n=10 : hardBrand$(n)="Atari" : hardTitle$(n)="Atari ST"
hardModel$(n)="AtariST" : hardCom$(n)="AtariST" : hardTV$(n)="Monitor"
hardWeek(n)=4 : hardMonth(n)=1 : hardYear(n)=1987
hardCost(n)=150000 : hardRetail(n)=10 : hardFormat(n)=2 : hardHandheld(n)=0
hardGameplay(n)=60 : hardReplay(n)=70
hard2D(n)=65 : hard3D(n)=60
hardSound(n)=65 : hardMusic(n)=65
n=11 : hardBrand$(n)="Commodore" : hardTitle$(n)="Amiga 500"
hardModel$(n)="Amiga" : hardCom$(n)="Amiga" : hardTV$(n)="Monitor"
hardWeek(n)=3 : hardMonth(n)=7 : hardYear(n)=1987
hardCost(n)=150000 : hardRetail(n)=10 : hardFormat(n)=2 : hardHandheld(n)=0
hardGameplay(n)=65 : hardReplay(n)=75
hard2D(n)=70 : hard3D(n)=55
hardSound(n)=70 : hardMusic(n)=70
;16-bit era
n=12 : hardBrand$(n)="Sega" : hardTitle$(n)="Megadrive"
hardModel$(n)="Mega" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=1 : hardMonth(n)=1 : hardYear(n)=1990
hardCost(n)=75000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=65 : hardReplay(n)=65
hard2D(n)=65 : hard3D(n)=45
hardSound(n)=65 : hardMusic(n)=65
n=13 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Super Nintendo"
hardModel$(n)="SNES" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=3 : hardMonth(n)=3 : hardYear(n)=1991
hardCost(n)=100000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=70
hard2D(n)=70 : hard3D(n)=50
hardSound(n)=70 : hardMusic(n)=70
n=37 : hardBrand$(n)="SNK" : hardTitle$(n)="Neo Geo"
hardModel$(n)="NeoGeo" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=1 : hardMonth(n)=10 : hardYear(n)=1990
hardCost(n)=125000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=65
hard2D(n)=75 : hard3D(n)=55
hardSound(n)=75 : hardMusic(n)=75
n=38 : hardBrand$(n)="Commodore" : hardTitle$(n)="Amiga CD32"
hardModel$(n)="CD32" : hardCom$(n)="Amiga" : hardTV$(n)="BoxTV"
hardWeek(n)=4 : hardMonth(n)=9 : hardYear(n)=1993
hardCost(n)=75000 : hardRetail(n)=10 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=70
hard2D(n)=70 : hard3D(n)=55
hardSound(n)=75 : hardMusic(n)=75
;32/64-bit era
n=14 : hardBrand$(n)="Atari" : hardTitle$(n)="Jaguar"
hardModel$(n)="Jaguar" : hardCom$(n)="AtariST" : hardTV$(n)="BoxTV"
hardWeek(n)=1 : hardMonth(n)=8 : hardYear(n)=1993
hardCost(n)=150000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=70
hard2D(n)=75 : hard3D(n)=65
hardSound(n)=75 : hardMusic(n)=75
n=15 : hardBrand$(n)="Sega" : hardTitle$(n)="Saturn"
hardModel$(n)="Saturn" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=1 : hardMonth(n)=7 : hardYear(n)=1995
hardCost(n)=100000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=80 : hardReplay(n)=75
hard2D(n)=80 : hard3D(n)=70
hardSound(n)=80 : hardMusic(n)=80
n=16 : hardBrand$(n)="Sony" : hardTitle$(n)="Playstation"
hardModel$(n)="PS1" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=4 : hardMonth(n)=9 : hardYear(n)=1995
hardCost(n)=100000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=80 : hardReplay(n)=80
hard2D(n)=80 : hard3D(n)=70
hardSound(n)=80 : hardMusic(n)=80
n=17 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Nintendo 64"
hardModel$(n)="N64" : hardCom$(n)="Keyboard" : hardTV$(n)="BoxTV"
hardWeek(n)=1 : hardMonth(n)=3 : hardYear(n)=1996
hardCost(n)=125000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=0
hardGameplay(n)=85 : hardReplay(n)=75
hard2D(n)=80 : hard3D(n)=70
hardSound(n)=75 : hardMusic(n)=75
;windows (tracks industry standards)
n=18 : hardBrand$(n)="Microsoft" : hardTitle$(n)="PC"
hardModel$(n)="Keyboard" : hardCom$(n)="Keyboard" : hardTV$(n)="Monitor"
hardWeek(n)=1 : hardMonth(n)=1 : hardYear(n)=1995
hardCost(n)=25000 : hardRetail(n)=10 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=80
hard2D(n)=70 : hard3D(n)=60
hardSound(n)=75 : hardMusic(n)=75
;128-bit era
n=19 : hardBrand$(n)="Sega" : hardTitle$(n)="Dreamcast"
hardModel$(n)="Dream" : hardCom$(n)="Keyboard" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=11 : hardYear(n)=1999
hardCost(n)=125000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=85 : hardReplay(n)=80
hard2D(n)=85 : hard3D(n)=75
hardSound(n)=85 : hardMusic(n)=85
n=20 : hardBrand$(n)="Sony" : hardTitle$(n)="Playstation 2"
hardModel$(n)="PS2" : hardCom$(n)="Keyboard" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=10 : hardYear(n)=2000
hardCost(n)=125000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=85 : hardReplay(n)=85
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=90 : hardMusic(n)=90
n=21 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Gamecube"
hardModel$(n)="Gamecube" : hardCom$(n)="Keyboard" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=8 : hardYear(n)=2001
hardCost(n)=150000 : hardRetail(n)=20 : hardFormat(n)=4 : hardHandheld(n)=0
hardGameplay(n)=90 : hardReplay(n)=80
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=80 : hardMusic(n)=80
n=22 : hardBrand$(n)="Microsoft" : hardTitle$(n)="X-Box"
hardModel$(n)="XBox" : hardCom$(n)="Keyboard" : hardTV$(n)="Monitor"
hardWeek(n)=1 : hardMonth(n)=11 : hardYear(n)=2001
hardCost(n)=125000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=85 : hardReplay(n)=85
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=90 : hardMusic(n)=90
;modern handhelds
n=23 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Gameboy Advance"
hardModel$(n)="Advance" : hardCom$(n)="Keyboard" : hardTV$(n)="Monitor"
hardWeek(n)=4 : hardMonth(n)=3 : hardYear(n)=2001
hardCost(n)=100000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=2
hardGameplay(n)=70 : hardReplay(n)=70
hard2D(n)=70 : hard3D(n)=50
hardSound(n)=70 : hardMusic(n)=70
n=24 : hardBrand$(n)="Nintendo" : hardTitle$(n)="DS"
hardModel$(n)="DS" : hardCom$(n)="Laptop" : hardTV$(n)="Laptop"
hardWeek(n)=1 : hardMonth(n)=12 : hardYear(n)=2004
hardCost(n)=125000 : hardRetail(n)=20 : hardFormat(n)=5 : hardHandheld(n)=3
hardGameplay(n)=80 : hardReplay(n)=80
hard2D(n)=80 : hard3D(n)=60
hardSound(n)=75 : hardMusic(n)=75
n=25 : hardBrand$(n)="Sony" : hardTitle$(n)="PSP"
hardModel$(n)="PSP" : hardCom$(n)="Laptop" : hardTV$(n)="Laptop"
hardWeek(n)=2 : hardMonth(n)=3 : hardYear(n)=2005
hardCost(n)=100000 : hardRetail(n)=20 : hardFormat(n)=4 : hardHandheld(n)=2
hardGameplay(n)=80 : hardReplay(n)=80
hard2D(n)=80 : hard3D(n)=70
hardSound(n)=80 : hardMusic(n)=80
;modern consoles
n=26 : hardBrand$(n)="Microsoft" : hardTitle$(n)="X-Box 360"
hardModel$(n)="XBox360" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=2 : hardYear(n)=2006
hardCost(n)=150000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=90 : hardReplay(n)=90
hard2D(n)=95 : hard3D(n)=90
hardSound(n)=95 : hardMusic(n)=95
n=27 : hardBrand$(n)="Sony" : hardTitle$(n)="Playstation 3"
hardModel$(n)="PS3" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=11 : hardYear(n)=2006
hardCost(n)=150000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=90 : hardReplay(n)=90
hard2D(n)=95 : hard3D(n)=90
hardSound(n)=95 : hardMusic(n)=95
n=28 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Wii"
hardModel$(n)="Wii" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=12 : hardYear(n)=2006
hardCost(n)=175000 : hardRetail(n)=15 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=95 : hardReplay(n)=85
hard2D(n)=95 : hard3D(n)=80
hardSound(n)=95 : hardMusic(n)=95
;apple
n=36 : hardBrand$(n)="Apple" : hardTitle$(n)="Mac"
hardModel$(n)="MacKeys" : hardCom$(n)="MacKeys" : hardTV$(n)="MacTV"
hardWeek(n)=1 : hardMonth(n)=1 : hardYear(n)=1998
hardCost(n)=100000 : hardRetail(n)=10 : hardFormat(n)=1 : hardHandheld(n)=0
hardGameplay(n)=70 : hardReplay(n)=80
hard2D(n)=70 : hard3D(n)=60
hardSound(n)=80 : hardMusic(n)=80
n=29 : hardBrand$(n)="Apple" : hardTitle$(n)="iPhone"
hardModel$(n)="iPhone" : hardCom$(n)="MacKeys" : hardTV$(n)="MacTV"
hardWeek(n)=1 : hardMonth(n)=6 : hardYear(n)=2007
hardCost(n)=200000 : hardRetail(n)=5 : hardFormat(n)=0 : hardHandheld(n)=1
hardGameplay(n)=75 : hardReplay(n)=70
hard2D(n)=80 : hard3D(n)=60
hardSound(n)=99 : hardMusic(n)=99
n=30 : hardBrand$(n)="Apple" : hardTitle$(n)="iPad"
hardModel$(n)="iPad" : hardCom$(n)="MacKeys" : hardTV$(n)="MacTV"
hardWeek(n)=1 : hardMonth(n)=6 : hardYear(n)=2011
hardCost(n)=200000 : hardRetail(n)=5 : hardFormat(n)=0 : hardHandheld(n)=2
hardGameplay(n)=80 : hardReplay(n)=75
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=99 : hardMusic(n)=99
;next gen
n=31 : hardBrand$(n)="Nintendo" : hardTitle$(n)="3DS"
hardModel$(n)="DS" : hardCom$(n)="Laptop" : hardTV$(n)="Laptop"
hardWeek(n)=4 : hardMonth(n)=3 : hardYear(n)=2011
hardCost(n)=150000 : hardRetail(n)=10 : hardFormat(n)=0 : hardHandheld(n)=3
hardGameplay(n)=90 : hardReplay(n)=90
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=90 : hardMusic(n)=90
n=32 : hardBrand$(n)="Sony" : hardTitle$(n)="Vita"
hardModel$(n)="Vita" : hardCom$(n)="Laptop" : hardTV$(n)="Laptop"
hardWeek(n)=1 : hardMonth(n)=12 : hardYear(n)=2011
hardCost(n)=125000 : hardRetail(n)=10 : hardFormat(n)=0 : hardHandheld(n)=2
hardGameplay(n)=90 : hardReplay(n)=90
hard2D(n)=90 : hard3D(n)=80
hardSound(n)=90 : hardMusic(n)=90
n=33 : hardBrand$(n)="Nintendo" : hardTitle$(n)="Wii U"
hardModel$(n)="WiiU" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=4 : hardYear(n)=2012
hardCost(n)=175000 : hardRetail(n)=10 : hardFormat(n)=0 : hardHandheld(n)=2
hardGameplay(n)=99 : hardReplay(n)=99
hard2D(n)=99 : hard3D(n)=90
hardSound(n)=99 : hardMusic(n)=99
n=34 : hardBrand$(n)="Sony" : hardTitle$(n)="Playstation 4"
hardModel$(n)="PS3" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=1 : hardYear(n)=2013
hardCost(n)=200000 : hardRetail(n)=10 : hardFormat(n)=0 : hardHandheld(n)=0
hardGameplay(n)=99 : hardReplay(n)=99
hard2D(n)=99 : hard3D(n)=99
hardSound(n)=99 : hardMusic(n)=99
n=35 : hardBrand$(n)="Microsoft" : hardTitle$(n)="X-Box 720"
hardModel$(n)="XBox" : hardCom$(n)="Laptop" : hardTV$(n)="BigTV"
hardWeek(n)=1 : hardMonth(n)=4 : hardYear(n)=2013
hardCost(n)=200000 : hardRetail(n)=10 : hardFormat(n)=0 : hardHandheld(n)=0
hardGameplay(n)=99 : hardReplay(n)=99
hard2D(n)=99 : hard3D(n)=99
hardSound(n)=99 : hardMusic(n)=99
;update list
;no_machines=n

;///////////////////////////////////////////////////////
;----------------- RELATED FUNCTIONS -------------------
;///////////////////////////////////////////////////////

;COUNT MACHINES
Function CountMachines(task) ;0=released, 1=owned, 2=active
 value=0
 For v=1 To no_machines
  If hardReleased(v)>0
   If task=0 Then value=value+1
   If task=1 And hardOwned(v)>0 Then value=value+1
   If task=2 And hardPromotion#(v)>-99 Then value=value+1
  EndIf
 Next
 Return value
End Function

;AVERAGE HARDWARE STATS
Function AverageHardwareStats(cyc)
 value=hardGameplay(cyc)+hardReplay(cyc)+hard2D(cyc)+hard3D(cyc)+hardSound(cyc)+hardMusic(cyc)
 value=value/6
 Return value
End Function

;FIND INDUSTRY STANDARDS
Function FindIndustryStandards()
 For cyc=1 To 7
  hardStandard(cyc)=0
 Next
 For v=1 To no_machines
  If hardReleased(v)>0
   If v<>18 And v<>36
    If hardGameplay(v)=>hardStandard(1) Then hardStandard(1)=hardGameplay(v) : hardLeader(1)=v
    If hardReplay(v)=>hardStandard(2) Then hardStandard(2)=hardReplay(v) : hardLeader(2)=v
    If hard2D(v)=>hardStandard(3) Then hardStandard(3)=hard2D(v) : hardLeader(3)=v
    If hard3D(v)=>hardStandard(4) Then hardStandard(4)=hard3D(v) : hardLeader(4)=v
    If hardSound(v)=>hardStandard(5) Then hardStandard(5)=hardSound(v) : hardLeader(5)=v
    If hardMusic(v)=>hardStandard(6) Then hardStandard(6)=hardMusic(v) : hardLeader(6)=v
   EndIf
   If AverageHardwareStats(v)=>hardStandard(7) Then hardStandard(7)=AverageHardwareStats(v) : hardLeader(7)=v
  EndIf
 Next
End Function

;CALCULATE HARDWARE SALES
Function GetHardwareSales(cyc)
 sales=0
 ;sales=sales+PercentOf#(hardGameplay(cyc),GetPercent#(hardGameplay(cyc),hardStandard(1)))
 ;sales=sales+PercentOf#(hardReplay(cyc),GetPercent#(hardReplay(cyc),hardStandard(2)))
 ;sales=sales+PercentOf#(hard2D(cyc),GetPercent#(hard2D(cyc),hardStandard(3)))
 ;sales=sales+PercentOf#(hard3D(cyc),GetPercent#(hard3D(cyc),hardStandard(4)))
 ;sales=sales+PercentOf#(hardSound(cyc),GetPercent#(hardSound(cyc),hardStandard(5)))
 ;sales=sales+PercentOf#(hardMusic(cyc),GetPercent#(hardMusic(cyc),hardStandard(6)))
 ;sales=sales*sales
 sales=sales+hardGameplay(cyc)
 sales=sales+hardReplay(cyc)
 sales=sales+hard2D(cyc)
 sales=sales+hard3D(cyc)
 sales=sales+hardSound(cyc)
 sales=sales+hardMusic(cyc)
 prowess=0
 prowess=prowess+GetPercent#(hardGameplay(cyc),hardStandard(1))
 prowess=prowess+GetPercent#(hardReplay(cyc),hardStandard(2))
 prowess=prowess+GetPercent#(hard2D(cyc),hardStandard(3))
 prowess=prowess+GetPercent#(hard3D(cyc),hardStandard(4))
 prowess=prowess+GetPercent#(hardSound(cyc),hardStandard(5))
 prowess=prowess+GetPercent#(hardMusic(cyc),hardStandard(6))
 sales=(sales*prowess)/2
 If optTime=1 Then sales=sales/2
 If hardUsers(cyc)>1000000 Then sales=sales/2
 sales=PercentOf#(sales,hardPromotion#(cyc))
 Return sales
End Function

;BRAND ACTIVE FOR EXCLUSIVITY?
Function BrandActive(brand$)
 value=0
 For v=1 To no_machines
  If hardReleased(v)>0 And hardBrand$(v)=brand$ And hardPromotion#(v)>-50 Then value=1
 Next
 Return value
End Function

;FORMAT IN USE?
Function FormatActive(format)
 value=0
 For v=1 To no_machines
  If hardReleased(v)>0 And hardFormat(v)=format And hardPromotion#(v)>-50 Then value=1
 Next
 Return value
End Function