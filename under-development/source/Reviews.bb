;//////////////////////////////////////////////////////////////////////////////
;--------------------------- UNDER DEVELOPMENT: REVIEWS -----------------------
;//////////////////////////////////////////////////////////////////////////////

;------------------------------------------------------------------------------
;////////////////////////////// 29. REVIEW ////////////////////////////////////
;------------------------------------------------------------------------------
Function Review()
;vary text choices
eNewspaper(1)=1
eAdvert(1)=12;Rnd(8,14) : negFormat=-1
If eAdvert(1)=<9 Then eAdvert(1)=Rnd(1,9) 
revRouteA=Rnd(0,2) : revRouteB=Rnd(0,2)
revRouteC=Rnd(0,2) : revRouteD=Rnd(0,2)
revRouteE=Rnd(0,2)
;calculate score
fed=charFed(gamChar)
FindIndustryStandards()
revScore(1)=GetPercent#(projGameplay(gamProject),hardStandard(1))
revScore(2)=GetPercent#(projReplay(gamProject),hardStandard(2))
revScore(3)=GetPercent#(proj2D(gamProject),hardStandard(3))
revScore(4)=GetPercent#(proj3D(gamProject),hardStandard(4))
revScore(5)=GetPercent#(projSound(gamProject),hardStandard(5))
revScore(6)=GetPercent#(projMusic(gamProject),hardStandard(6))
revScore(0)=revScore(1)+revScore(2)+revScore(5)+revScore(6)
If revScore(4)>revScore(3) Then revScore(0)=revScore(0)+revScore(4) Else revScore(0)=revScore(0)+revScore(3)
revScore(0)=revScore(0)/5
If revScore(0)>99 Then revScore(0)=99
If projReleased(gamProject)>0 Then revScore(0)=projReview(gamProject)
;find best feature
revBestFeature=0 : hi=-1
For count=1 To 6
 If revScore(count)>hi Then revBestFeature=count : hi=revScore(count)
Next
;guarantee credit
If projIdentified(gamProject)=<0 And projIdentity(gamProject)>0
 projIdentified(gamProject)=projIdentity(gamProject)
 LoadProjectBox(gamProject)
EndIf
If projIdentified(gamProject)>0 And projIdentity(gamProject)=<0
 projIdentity(gamProject)=projIdentified(gamProject)
 LoadProjectScreen(gamProject)
EndIf
For count=1 To 6
 If projCredit(gamProject,count)=0 Then projCredit(gamProject,count)=gamChar
Next
;identify port
port=0
For v=1 To no_projects
 If projReleased(v)>0 And projIdentity(v)=projIdentity(gamProject) And projFormat(v)<>projFormat(gamProject) Then port=1
Next
;character changes
If projReleased(gamProject)=0
 ;team credit
 For cyc=1 To optTeamSize
  char=charRoster(gamChar,cyc)
  If char>0
   If char=gamChar Then limit=5 Else limit=1
   charRep(char)=charRep(char)+PursueValue(charRep(char),revScore(0),limit)
   CheckLimits(char)
  EndIf
 Next 
 ;individual credit
 For cyc=1 To 6
  char=projCredit(gamProject,cyc)
  If cyc=1 Then charDesign(char)=charDesign(char)+PursueValue(charDesign(char),revScore(cyc),1)
  If cyc=2 Then charProgram(char)=charProgram(char)+PursueValue(charProgram(char),revScore(cyc),1)
  If cyc=3 Then char2D(char)=char2D(char)+PursueValue(char2D(char),revScore(cyc),1)
  If cyc=4 Then char3D(char)=char3D(char)+PursueValue(char3D(char),revScore(cyc),1)
  If cyc=5 Then charSound(char)=charSound(char)+PursueValue(charSound(char),revScore(cyc),1)
  If cyc=6 Then charMusic(char)=charMusic(char)+PursueValue(charMusic(char),revScore(cyc),1)
  charRep(char)=charRep(char)+PursueValue(charRep(char),revScore(cyc),5)
  CheckLimits(char)
 Next 
EndIf
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
foc=9 : cyc=0
go=0 : gotim=-10 : keytim=0
While go=0

 Cls
 frames=WaitTimer(timer)
 For framer=1 To frames
	
	;timers
	keytim=keytim-1
	If keytim<1 Then keytim=0
	
	;PROCESS
    gotim=gotim+1
	If gotim>50 And keytim=0
	 If KeyDown(1) Or MouseDown(1) Then go=1
	EndIf
	;reaction
	If gotim=0 Then ProduceSound(0,sPaper,22050,0.25)
	If gotim=5 
	 If revScore(0)=>70 Then ProduceSound(0,sCheer,0,Float#(revScore(0))/100)
	 If revScore(0)<70 Then PlaySound sGrumble 
    EndIf
    ;manage music
	ManageMusic(0)
	
 UpdateWorld
 Next 
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300) 
 If gotim>0
  ;magazine
  x=rX#(400) : y=GraphicsHeight()-294
  DisplayMagazine(1,x,y)
  ;game profile
  DrawImage projBox(gamProject),x-240,y-40
  DrawImage projScreen(gamProject),x-70,y-40
  DrawProjectProfile(gamProject,x+15,y-125,0)
  ;opening line
  ;Text newsX,newsY+(spacing*3),"Adam Bradbury's visuals have squeezed every last drop out of the Master System to be honest.",1,1
  SetFont fontNews(2) : Color 0,0,0
  newsX=x-290 : newsY=y+50 : spacing=25
  If revRouteA=0 And fed<no_feds Then namer$="'"+projTitle$(gamProject)+"' is the latest game that "+charName$(gamChar)+" has released under "+fedName$(fed)+":"
  If revRouteA=0 And fed=no_feds Then namer$="'"+projTitle$(gamProject)+"' is the latest effort from the independent developer "+charName$(gamChar)+":"
  If revRouteA=1 Then namer$="'"+projTitle$(gamProject)+"' is the latest game that "+charName$(gamChar)+" has produced for the "+hardTitle$(projFormat(gamProject))+":" 
  If revRouteA=2 And fed<no_feds Then namer$="This week sees the release of "+fedName$(fed)+"'s '"+projTitle$(gamProject)+"' for the "+hardTitle$(projFormat(gamProject))+":"
  If revRouteA=2 And fed=no_feds Then namer$="This week sees the release of "+charName$(gamChar)+"'s '"+projTitle$(gamProject)+"' for the "+hardTitle$(projFormat(gamProject))+":"
  If port=1 Then namer$=charName$(gamChar)+"'s latest release is a "+hardTitle$(projFormat(gamProject))+" version of '"+projTitle$(gamProject)+"':"
  Text newsX,newsY,namer$,0,1
  ;gameplay reviews
  If revScore(1)=>90
   If revScore(2)=>90
    lineA$="The intuitive gameplay makes it an absolute joy to interact with,"
    lineB$="and the sophisticated concept promises endless replay value."
   EndIf
   If revScore(2)=>70 And revScore(2)<90
    lineA$="This game spoils the player with irresistable gameplay - but the more"
    lineB$="they play, the more they'll crave the few features that are missing."
   EndIf
   If revScore(2)=>50 And revScore(2)<70
    lineA$="The game is incredibly fun to play in short bursts,"
    lineB$="but there's a distinct lack of substance in the long term."
   EndIf
   If revScore(2)=>30 And revScore(2)<50
    lineA$="The potential of this inventive concept shines through,"
    lineB$="but it's ultimately handicapped by flawed execution."
   EndIf
   If revScore(2)<30
    lineA$="There can be no doubt that this is an amazing gaming experience, but it's"
    lineB$="so shallow that it plays like a demo of the game it should have been!"
   EndIf
  EndIf
  If revScore(1)=>70 And revScore(1)<90
   If revScore(2)=>90
    lineA$="The gameplay isn't quite as smooth as we've come to expect, but"
    lineB$="the sheer depth to the concept will keep you playing for months."
   EndIf
   If revScore(2)=>70 And revScore(2)<90
    lineA$="Although purists will have their gripes, this is a solid gaming"
    lineB$="experience with interesting ideas that are well executed."
   EndIf
   If revScore(2)=>50 And revScore(2)<70
    lineA$="The gameplay is solid enough and can certainly be fun,"
    lineB$="but there's not enough to it to keep you coming back."
   EndIf
   If revScore(2)=>30 And revScore(2)<50
    lineA$="It's a nice concept with a lot of potential, but very little"
    lineB$="thought has gone into making it a meaningful experience."
   EndIf
   If revScore(2)<30
    lineA$="The game itself plays fine, but the execution is so dire"
    lineB$="that people will need a lot of patience to sit through it."
   EndIf
  EndIf
  If revScore(1)=>50 And revScore(1)<70
   If revScore(2)=>90
    lineA$="Playing the game can be an awkward experience, but the basic premise"
    lineB$="is so addictive that you'll find yourself being very forgiving."
   EndIf
   If revScore(2)=>70 And revScore(2)<90
    lineA$="The gameplay is flawed, to be sure, but there are hidden"
    lineB$="layers that will ensure you get some satisfaction from it."
   EndIf
   If revScore(2)=>50 And revScore(2)<70
    lineA$="Although this isn't a 'bad' game by any means, it's so"
    lineB$="predictable that it's hard to get excited about."
   EndIf
   If revScore(2)=>30 And revScore(2)<50
    lineA$="The game itself could be worse, but whatever qualities it"
    lineB$="has are made forgettable by a distinct lack of substance."
   EndIf
   If revScore(2)<30
    lineA$="There was the beginnings of a decent game here, but they"
    lineB$="seemed to give up and let the concept go to hell."
   EndIf
  EndIf
  If revScore(1)=>30 And revScore(1)<50
   If revScore(2)=>90
    lineA$="This is an incredibly sophisticated concept that deserved"
    lineB$="much better than the feeble gameplay it is lumbered with!"
   EndIf
   If revScore(2)=>70 And revScore(2)<90
    lineA$="The programmers have done a good job of making the game"
    lineB$="technically sound, but it simply isn't any fun to play."
   EndIf
   If revScore(2)=>50 And revScore(2)<70
    lineA$="If the game was simply more fun to play then some of the"
    lineB$="interesting ideas here would have had more chance of succeeding."
   EndIf
   If revScore(2)=>30 And revScore(2)<50
    lineA$="This is a miserable gaming experience that only promises"
    lineB$="frustration for those who bother to play it at length."
   EndIf
   If revScore(2)<30
    lineA$="This game's distinct lack of content merely serves to remind you that"
    lineB$="the pathetic concept wasn't worth bothering with in the first place."
   EndIf
  EndIf
  If revScore(1)<30
   If revScore(2)=>90
    lineA$="Some of the most impressive technical wizardry the "+hardTitle$(projFormat(gamProject))
    lineB$="has to offer has resulted in a game that simply isn't playable!"
   EndIf
   If revScore(2)=>70 And revScore(2)<90
    lineA$="The concept itself promises a lot, but has resulted in"
    lineB$="a dreadfully dull game that simply isn't worth playing."
   EndIf
   If revScore(2)=>50 And revScore(2)<70
    lineA$="The concept was nothing revolutionary to start with, so the"
    lineB$="miserable gameplay we have here is even harder to tolerate."
   EndIf
   If revScore(2)=>30 And revScore(2)<50
    lineA$="The only 'replay' value this game has is the urge you'll"
    lineB$="have to repeatedly smash your screen until it disappears!"
   EndIf
   If revScore(2)<30
    lineA$="This is an abysmal excuse for a 'game' that offers absolutely no"
    lineB$="entertainment of any kind. We're amazed that it even got released?!"
   EndIf
  EndIf
  If projReplay(gamProject)>hardReplay(projFormat(gamProject))-5 And hardReplay(projFormat(gamProject))<hardStandard(2)-5
   lineA$=charName$(projCredit(gamProject,2))+"'s programming squeezes the most out of the "+hardTitle$(projFormat(gamProject))+","
   lineB$="but players of more advanced systems will have come to expect more."
  EndIf 
  If projGameplay(gamProject)>hardGameplay(projFormat(gamProject))-5 And hardGameplay(projFormat(gamProject))<hardStandard(1)-5
   lineA$="The gameplay is impressive by the standards of the "+hardTitle$(projFormat(gamProject))+", but"
   lineB$="fans of more advanced systems will wonder what all the fuss is about."
  EndIf 
  Text newsX,newsY+spacing,lineA$,0,1
  Text newsX,newsY+(spacing*2),lineB$,0,1
  ;graphics reviews
  If revScore(4)=>90
   If revScore(3)=>90
    lineA$="The ground-breaking visuals are so incredible that you have to keep"
    lineB$="reminding yourself that you're playing a game and not watching a movie!"
   EndIf
   If revScore(3)=>70 And revScore(3)<90
    lineA$="We're treated to some of the most astonishing 3D graphics ever seen"
    lineB$="from a game, and they're complemented well by some charming 2D visuals."
   EndIf
   If revScore(3)=>50 And revScore(3)<70
    lineA$="The 3D world that "+charName$(projCredit(gamProject,4))+" has created is so mesmerizing"
    lineB$="that you can forgive the relatively disappointing 2D sprites."
   EndIf
   If revScore(3)=>30 And revScore(3)<50
    lineA$=charName$(projCredit(gamProject,4))+"'s incredible 3D graphics promise much, but the rest"
    lineB$="of the presentation seems to have been neglected by comparison."
   EndIf
   If revScore(3)<30
    lineA$="An enormous amount of work has go into trying to achieve something in 3D,"
    lineB$="but it comes at the expense of 2D visuals that are disappointingly bad."
   EndIf
  EndIf
  If revScore(4)=>70 And revScore(4)<90
   If revScore(3)=>90
    lineA$=charName$(projCredit(gamProject,3))+" has blessed this game with some incredible 2D visuals"
    lineB$="that have even managed to overshadow the impressive 3D elements."
   EndIf
   If revScore(3)=>70 And revScore(3)<90
    lineA$="The game asserts itself confidently with a united front of"
    lineB$="solid 2D and 3D visuals that complement each other well."
   EndIf
   If revScore(3)=>50 And revScore(3)<70
    lineA$="The game's perfunctory 2D graphics aren't quite equal to the"
    lineB$="high standard that had been set by the promising 3D visuals."
   EndIf
   If revScore(3)=>30 And revScore(3)<50
    lineA$=charName$(projCredit(gamProject,4))+"'s promising 3D visuals are let down"
    lineB$="by awkward 2D sprites that ruin the illusion."
   EndIf
   If revScore(3)<30
    lineA$="The game's 3D graphics are perfectly impressive, but it doesn't excuse"
    lineB$="the fact that the equally important 2nd dimension has been neglected."
   EndIf
  EndIf
  If revScore(4)=>50 And revScore(4)<70
   If revScore(3)=>90
    lineA$="This game's distinctly average 3D graphics take a backseat to"
    lineB$="the astonishing work that "+charName$(projCredit(gamProject,3))+" has been doing in 2D!"
   EndIf
   If revScore(3)=>70 And revScore(3)<90
    lineA$="The 3D elements are largely unimportant and have been"
    lineB$="confidently superseded by some impressive 2D stuff."
   EndIf
   If revScore(3)=>50 And revScore(3)<70
    lineA$="Unfortunately, the game's visuals are hard to get excited about."
    lineB$="Neither the 2D sprites nor the 3D elements are especially good."
   EndIf
   If revScore(3)=>30 And revScore(3)<50
    lineA$=charName$(projCredit(gamProject,4))+"'s questionable 3D visuals are made to look better"
    lineB$="than they actually are by comparison to the poor 2D elements."
   EndIf
   If revScore(3)<30
    lineA$="Graphically, the 3D elements would be passable if it"
    lineB$="wasn't for the atrocious 2D visuals that hurt your eyes!
   EndIf
  EndIf
  If revScore(4)=>30 And revScore(4)<50
   If revScore(3)=>90
    lineA$="Graphically, the focus is on "+charName$(projCredit(gamProject,3))+"'s sublime 2D work"
    lineB$="which overshadows the half-hearted attempts at 3D."
   EndIf
   If revScore(3)=>70 And revScore(3)<90
    lineA$="There are some awkward attempts to introduce 3D elements,"
    lineB$="but the core 2D graphics are charming enough to shine through."
   EndIf
   If revScore(3)=>50 And revScore(3)<70
    lineA$="The supposedly '3D' graphics are so feeble that the standard"
    lineB$="2D sprites are made to look positively beautiful by comparison."
   EndIf
   If revScore(3)=>30 And revScore(3)<50
    lineA$="Graphically, everything about this game is frustratingly"
    lineB$="ugly and threatens to ruin any fun that might be had."
   EndIf
   If revScore(3)<30
    lineA$="The game's dreadfully lazy graphics are not helped"
    lineB$="by the unconvincing attempts to achieve something in 3D."
   EndIf
  EndIf
  If revScore(4)<30
   If revScore(3)=>90
    lineA$=charName$(projCredit(gamProject,3))+" has blessed this game with 2D visuals so"
    lineB$="beautiful that it's like watching a Disney animation!"
   EndIf
   If revScore(3)=>70 And revScore(3)<90
    lineA$=charName$(projCredit(gamProject,3))+"'s charming 2D sprites are a real asset"
    lineB$="and give '"+projTitle$(gamProject)+"' its own unique identity."
   EndIf
   If revScore(3)=>50 And revScore(3)<70
    lineA$="The graphics serve their purpose without being especially revolutionary."
    lineB$="A little more attention to detail would have polished the experience."
   EndIf
   If revScore(3)=>30 And revScore(3)<50
    lineA$="The game's graphics are depressingly poor. In the absence of any"
    lineB$="3D to get excited about, the least gamers deserve is decent 2D."
   EndIf
   If revScore(3)<30
    lineA$="We can forgive the lack of 3D graphics, but the abysmal 2D graphics"
    lineB$="prove that "+charName$(gamChar)+" had no intention of making a decent game."
   EndIf
  EndIf
  If proj2D(gamProject)>hard2D(projFormat(gamProject))-5 And hard2D(projFormat(gamProject))<hardStandard(3)-5
   lineA$=charName$(projCredit(gamProject,3))+"'s 2D work pushes the "+hardTitle$(projFormat(gamProject))+" to its limit,"
   lineB$="but the graphics still pale in comparison to the "+hardTitle$(hardLeader(3))+"."
  EndIf 
  If proj3D(gamProject)>hard3D(projFormat(gamProject))-5 And hard3D(projFormat(gamProject))<hardStandard(4)-5
   lineA$=charName$(projCredit(gamProject,4))+"'s 3D visuals squeeze the most out of the "+hardTitle$(projFormat(gamProject))+","
   lineB$="but they no longer hold up in the era of the "+hardTitle$(hardLeader(4))+"."
  EndIf 
  Text newsX,newsY+(spacing*3),lineA$,0,1
  Text newsX,newsY+(spacing*4),lineB$,0,1
  ;sound reviews
  If revScore(6)=>90
   If revScore(5)=>90
    lineA$=charName$(projCredit(gamProject,6))+" has composed some classic tunes that transcend the world"
    lineB$="of gaming and wouldn't sound out of place on the radio or in a movie!"
   EndIf
   If revScore(5)=>70 And revScore(5)<90
    lineA$="The game features a fantastic score by "+charName$(projCredit(gamProject,6))+" - and although the"
    lineB$="sound effects aren't quite equal to the music, they're still impressive."
   EndIf
   If revScore(5)=>50 And revScore(5)<70
    lineA$="The fantastic music that "+charName$(projCredit(gamProject,6))+" has produced for the game only"
    lineB$="leads to disappointment that the sound effects aren't equal to it."
   EndIf
   If revScore(5)=>30 And revScore(5)<50
    lineA$="The sublime music that "+charName$(projCredit(gamProject,6))+" contributed is badly let down"
    lineB$="by feeble sound effects that threaten to ruin the experience."
   EndIf
   If revScore(5)<30
    lineA$="Rather depressingly, so much effort has gone into the superb music"
    lineB$="that nobody bothered to make any decent sound effects to go with it!"
   EndIf
  EndIf
  If revScore(6)=>70 And revScore(6)<90
   If revScore(5)=>90
    lineA$="The game sounds fantastic - with "+charName$(projCredit(gamProject,6))+"'s decent score"
    lineB$="surpassed only by the crystal clear, atmospheric effects."
   EndIf
   If revScore(5)=>70 And revScore(5)<90
    lineA$="The charming music really suits the game, and is perfectly"
    lineB$="complemented by some nice effects that shape the experience."
   EndIf
   If revScore(5)=>50 And revScore(5)<70
    lineA$="The game would have sounded better if the awkward sound effects"
    lineB$="were as good as the theme that "+charName$(projCredit(gamProject,6))+" composed for it."
   EndIf
   If revScore(5)=>30 And revScore(5)<50
    lineA$="Although the music is perfectly decent, it plays over a cacophony"
    lineB$="of unsuitable sound effects that threaten to ruin the experience."
   EndIf
   If revScore(5)<30
    lineA$=charName$(projCredit(gamProject,6))+"'s impressive music attempts to mask the fact that"
    lineB$="the rest of the game doesn't feature any decent sound effects!"
   EndIf
  EndIf
  If revScore(6)=>50 And revScore(6)<70
   If revScore(5)=>90
    lineA$="The game's music is distinctly average, but "+charName$(projCredit(gamProject,5))+" presides"
    lineB$="over some masterful sound effects that make it sound like a movie!"
   EndIf
   If revScore(5)=>70 And revScore(5)<90
    lineA$="Although the music is somewhat superfluous, "+charName$(projCredit(gamProject,5))+" offers" 
    lineB$="an array of impressive sound effects that draw you into the game."
   EndIf
   If revScore(5)=>50 And revScore(5)<70
    lineA$="The game's unadventurous sound adds very little to the experience - with"
    lineB$="stereotypical music and unconvincing effects that invite boredom."
   EndIf
   If revScore(5)=>30 And revScore(5)<50
    lineA$="The music is tolerable as far as games go, but the poor sound effects"
    lineB$="are harder to forgive because they make you resent getting involved."
   EndIf
   If revScore(5)<30
    lineA$="The half-decent music implies that the game had some potential,"
    lineB$="but this is all squandered by the intolerable sound effects."
   EndIf
  EndIf
  If revScore(6)=>30 And revScore(6)<50
   If revScore(5)=>90
    lineA$="The music is disappointing until you realize that the focus has"
    lineB$="been placed on creating a world of mesmerizing sound effects!"
   EndIf
   If revScore(5)=>70 And revScore(5)<90
    lineA$=charName$(projCredit(gamProject,5))+"'s charming sound effects complement the game well and promise"
    lineB$="much, but they're largely drowned out by the less impressive music."
   EndIf
   If revScore(5)=>50 And revScore(5)<70
    lineA$="Although the sound effects are tolerable as far as games go, the music"
    lineB$="is a constant annoyance that will have you reaching for the volume."
   EndIf
   If revScore(5)=>30 And revScore(5)<50
    lineA$="As far as the audio goes, the only 'fun' to be had is deciding"
    lineB$="which you hate worse out of the awful music and risible effects!"
   EndIf
   If revScore(5)<30
    lineA$="The feeble music is second only to the excruciating beeps"
    lineB$="and screeches that pass for sound effects in this game!"
   EndIf
  EndIf
  If revScore(6)<30
   If revScore(5)=>90
    lineA$="It's clear that the focus was on populating the world with stellar sound effects,"
    lineB$="but it's still hard to forgive the fact that no effort has gone into the music."
   EndIf
   If revScore(5)=>70 And revScore(5)<90
    g=GetGender(projCredit(gamProject,5))
    lineA$=charName$(projCredit(gamProject,5))+" has do a great job on the sound effects, so it's"
    lineB$="unfortunate that "+Lower$(His$(g))+" efforts are not complemented by any decent music."
   EndIf
   If revScore(5)=>50 And revScore(5)<70
    lineA$="The sound is nothing more than you would expect from a videogame, but"
    lineB$="the excruciating music doesn't belong in any piece of entertainment!"
   EndIf
   If revScore(5)=>30 And revScore(5)<50
    lineA$="Unfortunately, one of the most striking things about the game is how"
    lineB$="bad the music is - and the sound effects aren't much better either."
   EndIf
   If revScore(5)<30
    lineA$="Between the dreadful music and infuriating sound effects, the game"
    lineB$="sounds so bad that you literally have to play with the sound off!"
   EndIf
  EndIf
  If projSound(gamProject)>hardSound(projFormat(gamProject))-5 And hardSound(projFormat(gamProject))<hardStandard(5)-5
   lineA$="The sound "+charName$(projCredit(gamProject,5))+" has gotten out of the "+hardTitle$(projFormat(gamProject))+""
   lineB$="may be impressive, but it still makes the game feel old-fashioned."
  EndIf 
  If projMusic(gamProject)>hardMusic(projFormat(gamProject))-5 And hardMusic(projFormat(gamProject))<hardStandard(6)-5
   lineA$="Although "+charName$(projCredit(gamProject,6))+"'s music is impressive for the "+hardTitle$(projFormat(gamProject))+","
   lineB$="one suspects it would sound better coming out of the "+hardTitle$(hardLeader(6))+"."
  EndIf 
  Text newsX,newsY+(spacing*5),lineA$,0,1 
  Text newsX,newsY+(spacing*6),lineB$,0,1 
  ;conclusions
  If revBestFeature=1
   If revScore(0)=>90
    lineA$="'"+projTitle$(gamProject)+"' is simply the most fun you can have with"
    lineB$="your clothes on! Entertainment will never be the same again."
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$="'"+projTitle$(gamProject)+"' has its priorities straight with some great"
    lineB$="gameplay and can look forward to being enjoyed by the masses."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$=projTitle$(gamProject)+"'s gameplay gives it potential, but the"
    lineB$="experience ultimately needed to be more polished."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$=projTitle$(gamProject)+"'s admirable attempts at gameplay"
    lineB$="only serve to make it a disappointing release."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If revBestFeature=2
   If revScore(0)=>90
    lineA$="'"+projTitle$(gamProject)+"' is one of the most sophisticated gaming"
    lineB$="experiences of all time and proves that this IS an art form."
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$="Sophisticated releases like '"+projTitle$(gamProject)+"'"
    lineB$="are always welcome because they transcend gaming."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$=projTitle$(gamProject)+"'s attempts to achieve something special"
    lineB$="are admirable, but the content had to be equal to it."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$=projTitle$(gamProject)+"'s ambition has ultimately been its downfall"
    lineB$="because the concept simply doesn't hold together."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If revBestFeature=3
   If revScore(0)=>90
    lineA$=projTitle$(gamProject)+"'s incredible graphics are just one"
    lineB$="of many things that make the game a work of art!"
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$=projTitle$(gamProject)+"'s graphics are bound to attract"
    lineB$="attention and people will find much to enjoy here."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$="'"+projTitle$(gamProject)+"' isn't as good as it looks,"
    lineB$="but it may still find an audience."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$="Graphics don't necessarily make for a good game,"
    lineB$="and '"+projTitle$(gamProject)+"' is a prime example of that."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If revBestFeature=4
   If revScore(0)=>90
    lineA$=projTitle$(gamProject)+"'s incredible graphics are just one"
    lineB$="of many things that make the game a work of art!"
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$=projTitle$(gamProject)+"'s graphics are bound to attract"
    lineB$="attention and people will find much to enjoy here."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$="'"+projTitle$(gamProject)+"' isn't as good as it looks,"
    lineB$="but it may still find an audience."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$="Graphics don't necessarily make for a good game,"
    lineB$="and '"+projTitle$(gamProject)+"' is a prime example of that."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If revBestFeature=5
   If revScore(0)=>90
    lineA$=projTitle$(gamProject)+"'s incredible sound perfectly complements"
    lineB$="a game that also looks and feels like a movie!"
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$="'"+projTitle$(gamProject)+"' sounds good - quite literally - and"
    lineB$="will have a lot of people lending their ears to it."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$="'"+projTitle$(gamProject)+"' isn't as good as it sounds,"
    lineB$="but there are some interesting ideas here."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$="Sound is perhaps the least important aspect of any game,"
    lineB$="and '"+projTitle$(gamProject)+"' comes up short where it matters."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If revBestFeature=6
   If revScore(0)=>90
    lineA$=projTitle$(gamProject)+"'s incredible music perfectly complements"
    lineB$="a game that also looks and feels like a movie!"
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$="'"+projTitle$(gamProject)+"' sounds good - quite literally - and"
    lineB$="will have a lot of people lending their ears to it."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$="'"+projTitle$(gamProject)+"' isn't as good as it sounds,"
    lineB$="but there are some interesting ideas here."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$="Good music complements a game instead of making one, and"
    lineB$="'"+projTitle$(gamProject)+"' simply doesn't have much to compliment."
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' has brought shame on the "+hardTitle$(projFormat(gamProject))+","
    lineB$="which is capable of better games than this."
   EndIf
  EndIf
  If TeamSize(gamChar)=<1
   If revScore(0)=>90
    lineA$="'"+projTitle$(gamProject)+"' is so good that you'll swear it"
    lineB$="was made by hundreds of people instead of just one!"
   EndIf
   If revScore(0)=>70 And revScore(0)<90
    lineA$="'"+projTitle$(gamProject)+"' is impressive stuff for the work"
    lineB$="of just one person and deserves to be recognized."
   EndIf
   If revScore(0)=>50 And revScore(0)<70
    lineA$="It's admirable that '"+projTitle$(gamProject)+"' was made by one"
    lineB$="person, but the game itself doesn't offer much to admire."
   EndIf
   If revScore(0)=>30 And revScore(0)<50
    lineA$="'"+projTitle$(gamProject)+"' is the work of just one person,"
    lineB$="and it shows because it simply isn't any good!"
   EndIf
   If revScore(0)<30
    lineA$="'"+projTitle$(gamProject)+"' is proof, were it ever needed, that"
    lineB$="games should be made by teams instead of individuals."
   EndIf
  EndIf
  If AverageProjectStats(gamProject,1)>AverageHardwareStats(projFormat(gamProject))-5 And AverageHardwareStats(projFormat(gamProject))<hardStandard(7)-10
   lineA$="'"+projTitle$(gamProject)+"' may be one of the best games for the "+hardTitle$(projFormat(gamProject))+","
   lineB$="but it will struggle to stay relevant in the era of the "+hardTitle$(hardLeader(7))+"."
  EndIf 
  ;lineA$="'"+projTitle$(gamProject)+"' is the most fun you"
  ;lineB$="can have with your clothes on!"
  Text newsX,(newsY+4)+(spacing*7),lineA$,0,1
  Text newsX,(newsY+4)+(spacing*8),lineB$,0,1 
  ;score
  SetFont fontNews(20)
  Outline(revScore(0)+"%",x+250,y+225,0,0,0,255,50,50)
  ;prompt
  If gotim>50
   SetFont fontNews(2)
   Outline(">>> CLICK TO PROCEED >>>",x+5,GraphicsHeight()-15,100,100,100,250,250,250)
  EndIf
 EndIf
 ;cursor
 DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
PlaySound sMenuGo
FreeTimer timer
;release project
If projReleased(gamProject)=0
 gamRelease=gamProject
 projReleased(gamRelease)=gamDate
 projReview(gamRelease)=revScore(0)
 projSales(gamRelease)=0
 chartPromotion#(1)=70+(revScore(0)/2)
 If port>0 Then chartPromotion#(1)=chartPromotion#(1)-25
 If gamAgreement(1)>0 Then chartPromotion#(1)=chartPromotion#(1)+25 : gamAgreement(1)=0
 If (gamMission=>1 And gamMission=<6) Or (gamMission=14 And gamRelease=gamTarget) Then gamMission=0
 chartList(1)=0 : chartRank(1)=0 : chartLimited(1)=0
 screen=23
Else
 screen=20
EndIf
End Function

;/////////////////////////////////////////////////////////////////////////////////////
;------------------------------ RELATED FUNCTIONS ------------------------------------
;/////////////////////////////////////////////////////////////////////////////////////

;AVERAGE PROJECT STATS
Function AverageProjectStats(cyc,combineGFX)
 If combineGFX>0
  value=projGameplay(cyc)+projReplay(cyc)+projSound(cyc)+projMusic(cyc)
  If GetPercent#(proj2D(gamProject),hardStandard(3))>GetPercent#(proj3D(gamProject),hardStandard(4))
   value=value+proj2D(gamProject)
  Else
   value=value+proj3D(gamProject)
  EndIf
  value=value/5
 Else
  value=projGameplay(cyc)+projReplay(cyc)+proj2D(cyc)+proj3D(cyc)+projSound(cyc)+projMusic(cyc)
  value=value/6
 EndIf
 Return value
End Function