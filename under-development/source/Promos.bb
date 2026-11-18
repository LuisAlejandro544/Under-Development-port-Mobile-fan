;//////////////////////////////////////////////////////////////////////////////
;--------------------------- UNDER DEVELOPMENT: PROMOS ------------------------
;//////////////////////////////////////////////////////////////////////////////

;DISPLAY PROMO LINES
Function DisplaySubtitles()
 ;set measurements
 width=760 : height=40
 ;white text
 SqueezeNewsFont(lineA$,rX#(width),rY#(height))
 If lineB$="" And greenLineB$="" And redLineB$="" Then y=535 Else y=515
 Outline(lineA$,rX#(400),rY#(y),25,25,25,250,250,250)
 SqueezeNewsFont(lineB$,rX#(width),rY#(height))
 Outline(lineB$,rX#(400),rY#(560),25,25,25,250,250,250)
 ;green text
 SqueezeNewsFont(greenLineA$,rX#(width),rY#(height))
 If lineB$="" And greenLineB$="" And redLineB$="" Then y=535 Else y=515
 Outline(greenLineA$,rX#(400),rY#(y),5,25,5,100,200,100)
 SqueezeNewsFont(greenLineB$,rX#(width),rY#(height))
 Outline(greenLineB$,rX#(400),rY#(560),5,25,5,100,200,100)
 ;red text
 SqueezeNewsFont(redLineA$,rX#(width),rY#(height))
 If lineB$="" And greenLineB$="" And redLineB$="" Then y=535 Else y=515
 Outline(redLineA$,rX#(400),rY#(y),25,5,5,200,100,100)
 SqueezeNewsFont(redLineB$,rX#(width),rY#(height))
 Outline(redLineB$,rX#(400),rY#(560),25,5,5,200,100,100)
End Function

;MAKE CHARACTER SPEAK
Function Speak(cyc,target,expression)
 ;switch focus
 camFoc=cyc : camType=10
 If target=>0 And target<>cyc Then pFoc(cyc)=target
 For v=1 To no_plays
  If v<>cyc Then pFoc(v)=cyc 
 Next
 ;expression
 pSpeaking(cyc)=1 : speaker=cyc
 If expression>0 Then pEyes(cyc)=expression
 ;background
 Color 0,0,0
 Rect rX#(0),rY#(480),rX#(800),rY#(600),1
End Function

;RESET EXPRESSIONS
Function ResetExpressions(cyc) 
 pOldEyes(cyc)=pEyes(cyc)
 pSpeaking(cyc)=0
End Function

;APPLY FACIAL EXPRESSIONS
Function FacialExpressions(cyc)
 ;eye expressions 
 If pEyes(cyc)<1 Then pEyes(cyc)=1
 If pEyes(cyc)>3 Then pEyes(cyc)=3
 If pEyes(cyc)<>pOldEyes(cyc) And pEyes(cyc)>0
  EntityTexture pLimb(cyc,1),tEyes(pEyes(cyc)),0,3
  pOldEyes(cyc)=pEyes(cyc)
 EndIf
 ;mouth expressions
 If pSpeaking(cyc)=1
  randy=Rnd(0,3)
  If randy=0 Then pMouth(cyc)=Rnd(-2,5)
  If pMouth(cyc)<0 Then pMouth(cyc)=0
  EntityTexture pLimb(cyc,1),tMouth(pMouth(cyc)),0,4
 EndIf
 If pSpeaking(cyc)=0 And pMouth(cyc)>0
  pMouth(cyc)=0
  EntityTexture pLimb(cyc,1),tMouth(0),0,4
 EndIf
End Function