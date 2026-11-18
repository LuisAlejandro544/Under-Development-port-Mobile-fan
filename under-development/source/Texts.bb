;//////////////////////////////////////////////////////////////////////////////
;--------------------------- UNDER DEVELOPMENT: TEXTS -------------------------
;//////////////////////////////////////////////////////////////////////////////

;///////////////////////////////////////////////////////
;--------------------- FONTS ------------------------
;///////////////////////////////////////////////////////
.Fonts
Dim font(30)
Dim fontNews(20)
Function LoadFonts()
 ;main font 
 size=10
 For count=0 To 30
  font(count)=LoadFont("Manaspace Regular.ttf",size,0,0,0)
  If count>20 Then size=size+3 Else size=size+1
  If count=1 Then size=size+1
 Next
 ;news font (3=news, 8=promo, 10=headline)
 fontNews(0)=LoadFont("Times New Roman",12,0,0,0)
 size=15
 For count=1 To 20
  fontNews(count)=LoadFont("Times New Roman",size,1,0,0)
  size=size+3
 Next 
End Function

;//////////////////////////////////////////////////////
;------------------- KEY NAMES ------------------------
;//////////////////////////////////////////////////////
Dim Key$(255)
For count=0 To 255
 Key$(count)="?" 
Next
Key$(2)="1" : Key$(3)="2" : Key$(4)="3" : Key$(5)="4" : Key$(6)="5"
Key$(7)="6" : Key$(8)="7" : Key$(9)="8" : Key$(10)="9" : Key$(11)="0"
Key$(12)="-" : Key$(13)="+" : Key$(14)="Backspace" : Key$(15)="Tab"
Key$(16)="Q" : Key$(17)="W" : Key$(18)="E" : Key$(19)="R" : Key$(20)="T" : Key$(21)="Y"
Key$(22)="U" : Key$(23)="I" : Key$(24)="O" : Key$(25)="P" : Key$(26)="[" : Key$(27)="]"
Key$(29)="Left Ctrl" : Key$(30)="A" : Key$(31)="S" : Key$(32)="D" : Key$(33)="F" : Key$(34)="G"
Key$(35)="H" : Key$(36)="J" : Key$(37)="K" : Key$(38)="L" : Key$(39)=";" : Key$(40)="'" : Key$(41)="#"
Key$(42)="Left Shift" : Key$(43)="\" : Key$(44)="Z" : Key$(45)="X" : Key$(46)="C" : Key$(47)="V"
Key$(48)="B" : Key$(49)="N" : Key$(50)="M" : Key$(51)="," : Key$(52)="." : Key$(53)="/"
Key$(54)="Right Shift" : Key$(56)="Left Alt" : Key$(157)="Right Ctrl" : Key$(184)="Right Alt"
Key$(57)="Space" : Key$(200)="Cursor Up" : Key$(208)="Cursor Down" 
Key$(203)="Cursor Left" : Key$(205)="Cursor Right"

;//////////////////////////////////////////////////////
;-------------------- STATUS --------------------------
;//////////////////////////////////////////////////////
;weeks
Dim textWeek$(4)
textWeek$(1)="1st week"
textWeek$(2)="2nd week"
textWeek$(3)="3rd week"
textWeek$(4)="4th week"
;months
Dim textMonth$(12)
textMonth$(1)="January"
textMonth$(2)="February"
textMonth$(3)="March"
textMonth$(4)="April"
textMonth$(5)="May"
textMonth$(6)="June"
textMonth$(7)="July"
textMonth$(8)="August"
textMonth$(9)="September"
textMonth$(10)="October"
textMonth$(11)="November"
textMonth$(12)="December"
;time frame
Dim textPeriod$(3)
textPeriod$(1)="week"
textPeriod$(2)="month"
textPeriod$(3)="year"
;attributes
Dim textAttribute$(6)
textAttribute$(1)="design"
textAttribute$(2)="programming"
textAttribute$(3)="2D graphics"
textAttribute$(4)="3D graphics"
textAttribute$(5)="sound"
textAttribute$(6)="music"
;roles
Dim textRole$(6)
textRole$(1)="designer"
textRole$(2)="programmer"
textRole$(3)="2D artist"
textRole$(4)="3D artist"
textRole$(5)="sound engineer"
textRole$(6)="musician"
;fashion
Dim textFashion$(6)
textFashion$(1)="Gameplay"
textFashion$(2)="Replay"
textFashion$(3)="2D Graphics"
textFashion$(4)="3D Graphics"
textFashion$(5)="Sound"
textFashion$(6)="Music"
;policies
Dim textPolicy$(20)
textPolicy$(1)="only making games for certain consoles"
textPolicy$(2)="paying generous advances instead of royalties"
textPolicy$(3)="paying generous royalties instead of advances"
textPolicy$(4)="signing employees to long-term contracts."
textPolicy$(5)="signing employees to short-term contracts."
textPolicy$(6)="only releasing games of a certain quality."
textPolicy$(7)="recruiting talent from other companies."
textPolicy$(8)="releasing games as soon as possible."
textPolicy$(9)="subsidising the rent for employees."
;race
Dim textRace$(5)
textRace$(0)="white"
textRace$(1)="white"
textRace$(2)="Asian"
textRace$(3)="black"
;gender contexts
Global g
Dim He$(2)
He$(0)="He"
He$(1)="She"
He$(2)="They"
Dim Him$(2)
Him$(0)="him"
Him$(1)="her"
Him$(2)="them"
Dim His$(2)
His$(0)="His"
His$(1)="Her"
His$(2)="Their"
;group contexts
Dim We$(1)
We$(0)="I"
We$(1)="We"
Dim Were$(1)
Were$(0)="I'm"
Were$(1)="We're"
Dim Weve$(1)
Weve$(0)="I've"
Weve$(1)="We've"
Dim Our$(1)
Our$(0)="My"
Our$(1)="Our"
Dim Ours$(1)
Ours$(0)="Mine"
Ours$(1)="Ours"
Dim Us$(1)
Us$(0)="me"
Us$(1)="us"
Dim Are$(1)
Are$(0)="is"
Are$(1)="are"
Dim Have$(1)
Have$(0)="has"
Have$(1)="have"
Dim Artist$(1)
Artist$(0)="artist"
Artist$(1)="band"

;//////////////////////////////////////////////////////
;------------------- OPTIONS --------------------------
;//////////////////////////////////////////////////////
;on/off
Dim textOnOff$(1)
textOnOff$(0)="Off"
textOnOff$(1)="On"
;yes/no
Dim textYesNo$(1)
textYesNo$(0)="No"
textYesNo$(1)="Yes"
;resolution
Global no_resolutions=10
Dim textResX$(no_resolutions),textResY$(no_resolutions)
textResX$(0)="320" : textResY$(0)="240"
textResX$(1)="640" : textResY$(1)="480"
textResX$(2)="800" : textResY$(2)="600"
textResX$(3)="1024" : textResY$(3)="768"
textResX$(4)="1280" : textResY$(4)="800"
textResX$(5)="1280" : textResY$(5)="1024"
textResX$(6)="1366" : textResY$(6)="768"
textResX$(7)="1600" : textResY$(7)="900"
textResX$(8)="1600" : textResY$(8)="1200"
textResX$(9)="1900" : textResY$(9)="1200"
textResX$(10)="1920" : textResY$(10)="1080"
;shadows
Dim textShadows$(3)
textShadows$(0)="None"
textShadows$(1)="Basic"
textShadows$(2)="Animated"
;jumble
Dim textJumble$(2)
textJumble$(0)="Default"
textJumble$(1)="Random Draft"
textJumble$(2)="Random Stats"
;cutscenes
Dim textCutscenes$(2)
textCutscenes$(0)="Only When Necessary"
textCutscenes$(1)="Only For Hardware"
textCutscenes$(2)="Wherever Possible"
;entourage
Dim textEntourage$(2)
textEntourage$(0)="None"
textEntourage$(1)="Select Few"
textEntourage$(2)="All"
;character selection
Dim textGrid$(2)
textGrid$(0)="List Of Names"
textGrid$(1)="Grid Of Names"
;sub-games
Dim textGame$(20)
textGame$(1)="Tetris"
textGame$(2)="Space Invaders"
textGame$(3)="Breakout"
textGame$(4)="Asteroids"
textGame$(5)="Moksha"
textGame$(6)="Snake"
textGame$(7)="Sure Shot"
textGame$(8)="Crosshair"

;//////////////////////////////////////////////////////
;-------------------- EDITOR --------------------------
;//////////////////////////////////////////////////////
;gender
Dim textGender$(2)
textGender$(0)="Male"
textGender$(1)="Female"
;eyes
Dim textEyes$(3)
textEyes$(1)="Angry"
textEyes$(2)="Neutral"
textEyes$(3)="Docile"
;build
Dim textBuild$(10)
textBuild$(0)="Female"
textBuild$(1)="Gaunt"
textBuild$(2)="Slim"
textBuild$(3)="Normal"
textBuild$(4)="Muscular"
textBuild$(5)="Chubby"
textBuild$(6)="Fat"
textBuild$(7)="Obese"
;bagginess
Dim textBaggy$(3)
textBaggy$(0)="Tight"
textBaggy$(1)="Baggy Top"
textBaggy$(2)="Baggy Pants"
textBaggy$(3)="All Baggy"
;hats
Dim textHat$(no_hatstyles)
textHat$(0)="None"
textHat$(1)="Headband"
textHat$(2)="Bandana"
textHat$(3)="Straight Cap"
textHat$(4)="Tipped Cap"
textHat$(5)="Raised Cap"
textHat$(6)="Reversed Cap"
textHat$(7)="Straight Hat"
textHat$(8)="Tipped Hat"
textHat$(9)="Raised Hat"
;specs
Dim textSpecs$(no_specs)
textSpecs$(0)="None"
textSpecs$(1)="Gold Spectacles"
textSpecs$(2)="Silver Spectacles"
textSpecs$(3)="Black Spectacles"
textSpecs$(4)="Amber Lenses"
textSpecs$(5)="Yellow Lenses"
textSpecs$(6)="Black Shades"
textSpecs$(7)="Gold Shades"
textSpecs$(8)="Eye Patch"
;accessories
Dim textAccessory$(no_accessories)
textAccessory$(0)="None"
textAccessory$(1)="Rings"
textAccessory$(2)="Earrings"
textAccessory$(3)="Necklace"
textAccessory$(4)="Bracelet"
textAccessory$(5)="Watch"
textAccessory$(6)="All (Except Earrings)"
textAccessory$(7)="All (With Earrings)"
;hair styles
Dim textHair$(no_hairstyles)
textHair$(0)="Bald"
textHair$(1)="Bald Shave"
textHair$(2)="Receding Shave"
textHair$(3)="Light Shave"
textHair$(4)="Dark Shave"
textHair$(5)="Balding"
textHair$(6)="Lengthy Balding"
textHair$(7)="Receding"
textHair$(8)="Short"
textHair$(9)="Raised"
textHair$(10)="Quiff"
textHair$(11)="Side Parting"
textHair$(12)="Centre Parting"
textHair$(13)="Fringe"
textHair$(14)="Thick"
textHair$(15)="Lengthy"
textHair$(16)="Small Afro"
textHair$(17)="Big Afro"
textHair$(18)="Wedge"
textHair$(19)="Spikey"
textHair$(20)="Mohican"
textHair$(21)="Corn Rows"
textHair$(22)="Balding w/ Ponytail"
textHair$(23)="Receding w/ Ponytail"
textHair$(24)="Short w/ Ponytail"
textHair$(25)="Raised w/ Ponytail"
textHair$(26)="Quiff w/ Ponytail"
textHair$(27)="Side Parting w/ Ponytail"
textHair$(28)="Centre Parting w/ Ponytail"
textHair$(29)="Fringe w/ Ponytail"
textHair$(30)="Thick w/ Ponytail"
textHair$(31)="Small Afro w/ Ponytail"
textHair$(32)="Mohican w/ Ponytail"
textHair$(33)="Corn Rows w/ Ponytail"
textHair$(34)="Balding w/ Length"
textHair$(35)="Receding w/ Length"
textHair$(36)="Short w/ Length"
textHair$(37)="Raised w/ Length"
textHair$(38)="Quiff w/ Length"
textHair$(39)="Side Parting w/ Length"
textHair$(40)="Centre Parting w/ Length"
textHair$(41)="Fringe w/ Length"
textHair$(42)="Thick w/ Length"
textHair$(43)="Small Afro w/ Length"
textHair$(44)="Mohican w/ Length"
textHair$(45)="Corn Rows w/ Length"
textHair$(46)="Pointed Shave"
textHair$(47)="Wedge Shave"
;hair references
Dim hairFile$(20)
hairFile$(1)="H_Short"
hairFile$(2)="H_Part"
hairFile$(3)="H_Mop"
hairFile$(4)="H_Recede"
hairFile$(5)="H_Bald"
hairFile$(6)="H_Pony"
hairFile$(7)="H_Long"
hairFile$(8)="H_Spikey"
hairFile$(9)="H_Afro"
hairFile$(10)="H_Rows"
hairFile$(11)="H_Full"
hairFile$(12)="H_Oaf"
hairFile$(13)="H_Raised"
hairFile$(14)="H_Quiff"
hairFile$(15)="H_Buzz"
hairFile$(16)="H_Thick"
hairFile$(17)="H_Curly"
hairFile$(18)="H_Punk"
hairFile$(19)="H_Side"