;//////////////////////////////////////////////////////////////////////////////
;-------------------------- UNDER DEVELOPMENT: SUB-GAMES ----------------------
;//////////////////////////////////////////////////////////////////////////////

;//////////////////////////////////////////////////////////////////////////////
;------------------------------- 61. TETRIS -----------------------------------
;//////////////////////////////////////////////////////////////////////////////

;VARIABLES
Global tetFallTim#,tetFallSpeed#,tetSettleTim
Global tetPiece,tetNextPiece,no_pieces
Global tetType,tetNextType
Global tetPieceX,tetPieceY,tetAngle
Global tetPieceOldX,tetPieceOldY
Dim tetWell(10,16)
Dim tetWellX(10)
Dim tetWellY(16)
Dim tetShape(10,4,5,5)
;key
n=1
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;line
n=2
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=1 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=1 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=1 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=1 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;box
n=3
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;z1
n=4
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=1 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=1 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;z2
n=5
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=1 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=1 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=1 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=1 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;L1
n=6
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=1 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=1 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=1 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
;L2
n=7
a=4
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=1 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=1
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=1 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=2
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=0 : tetShape(n,a,3,4)=1 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=0 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=0 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=1 : tetShape(n,a,3,2)=1 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
a=3
tetShape(n,a,1,5)=0 : tetShape(n,a,2,5)=0 : tetShape(n,a,3,5)=0 : tetShape(n,a,4,5)=0 : tetShape(n,a,5,5)=0
tetShape(n,a,1,4)=0 : tetShape(n,a,2,4)=1 : tetShape(n,a,3,4)=0 : tetShape(n,a,4,4)=0 : tetShape(n,a,5,4)=0
tetShape(n,a,1,3)=0 : tetShape(n,a,2,3)=1 : tetShape(n,a,3,3)=1 : tetShape(n,a,4,3)=1 : tetShape(n,a,5,3)=0
tetShape(n,a,1,2)=0 : tetShape(n,a,2,2)=0 : tetShape(n,a,3,2)=0 : tetShape(n,a,4,2)=0 : tetShape(n,a,5,2)=0
tetShape(n,a,1,1)=0 : tetShape(n,a,2,1)=0 : tetShape(n,a,3,1)=0 : tetShape(n,a,4,1)=0 : tetShape(n,a,5,1)=0
Global no_shapes=n

;GAME
Function Tetris()
;difficulty
GetGameSettings()
tetFallSpeed#=1.5-(Float#(gamSkill)/100)
no_pieces=gamLimit : gamMaxScore=gamLimit*3
For count=0 To 6
 gamScore(count)=0 : gamNewScore(count)=0
Next
;plot well
x=rX#(400)-300 : y=rY#(300)+260
For count=1 To 10
 tetWellX(count)=x : x=x+35
Next
For count=1 To 16
 tetWellY(count)=y : y=y-35
Next
;reset status
For x=1 To 10
 For y=1 To 16
  tetWell(x,y)=0
  If y=<5
   randy=Rnd(0,1+(y*2))
   If randy=<1 Then tetWell(x,y)=8
  EndIf
 Next
Next
tetFallTim#=15.0
tetPiece=0 : tetNextPiece=Rnd(1,no_shapes)
If gamDevelop=>1 And gamDevelop=<6 Then tetNextType=gamDevelop Else tetNextType=Rnd(1,6)
If gamDevelop=>1 And gamDevelop=<6
 For n=1 To no_shapes
  ConformPiece(n,gamDevelop)
 Next
EndIf
;explosions
exCount=0
For v=1 To no_explodes
 exState(v)=0 : exTim(v)=0
Next
;frame rating
timer=CreateTimer(30)
;MAIN LOOP
screenState=0
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
	 ;quit
	 If KeyDown(1) And screenState=1
	  PlaySound sFail : keytim=20
	  screenState=-1 : gotim=0
	  For count=0 To 6
	   gamNewScore(count)=gamNewScore(count)/2
	  Next
	 EndIf
	 ;proceed
	 If KeyDown(1) Or MouseDown(1) Or DirectionPressed() Or ButtonPressed(1) Or (screenState<0 And gotim>200) Or (screenState=0 And gotim>300)
	  If screenState<0 And gamScore(0)=gamNewScore(0) And gotim>50 Then go=1
	  If screenState=0 Then GetNewPiece() : screenState=1 : gotim=0 : keytim=20
	 EndIf
	EndIf
	;manage music
	ManageMusic(0) 
	
	;PIECE
	If screenState=1 And tetPiece>0
	 ;movement
	 tetPieceOldX=tetPieceX
	 tetFallTim#=tetFallTim#-tetFallSpeed#
	 If KeyDown(208) Or JoyYDir()>0 Then tetFallTim#=tetFallTim#-10.0
	 If tetFallTim#<0 Then tetFallTim#=0  
	 If tetFallTim#=0 Then tetPieceOldY=tetPieceY : tetPieceY=tetPieceY-1 : tetFallTim#=15.0
	 If gotim>20 And keytim=0
	  If KeyDown(203) Or JoyXDir()<0 Then tetPieceX=tetPieceX-1 : keytim=5
	  If KeyDown(205) Or JoyXDir()>0 Then tetPieceX=tetPieceX+1 : keytim=5
	  ;rotate
	  If (ButtonPressed(1) Or KeyDown(200)) And tetPieceY>1
	   PlaySound sMenuBrowse : keytim=5
	   tetAngle=tetAngle+1
	   If tetAngle>4 Then tetAngle=1
       While SpillOut()=1
        tetPieceX=tetPieceX+1
       Wend
       While SpillOut()=2
        tetPieceX=tetPieceX-1
       Wend
       While SpillOut()=3
        tetPieceY=tetPieceY+1
       Wend
      EndIf
     EndIf
     ;horizontal collision detection
     For subX=1 To 5
      For subY=1 To 5
       showX=tetPieceX+(subX-3) : showY=tetPieceY+(subY-3)
       If tetShape(tetPiece,tetAngle,subX,subY)>0
        If showX<1 Or showX>10 Then tetPieceX=tetPieceOldX
        If showX=>1 And showX=<10 And showY=>1 And showY=<16
         If tetWell(showX,showY)>0 Then tetPieceX=tetPieceOldX
        EndIf
       EndIf
      Next
     Next
     ;vertical collision detection
     For subX=1 To 5
      For subY=1 To 5
       showX=tetPieceX+(subX-3) : showY=tetPieceY+(subY-3)
       If tetShape(tetPiece,tetAngle,subX,subY)>0
        If showY<1 Then tetPieceY=tetPieceOldY
        If showX=>1 And showX=<10 And showY=>1 And showY=<16
         If tetWell(showX,showY)>0 Then tetPieceY=tetPieceOldY
        EndIf
       EndIf
      Next
     Next 
     ;settled
     If tetPieceY=tetPieceOldY ;And tetPieceOldY<16
      PlaySound sBall
      For subX=1 To 5
       For subY=1 To 5
        showX=tetPieceX+(subX-3) : showY=tetPieceY+(subY-3)
        If showX=>1 And showX=<10 And showY=>1 And showY=<16 
         If tetShape(tetPiece,tetAngle,subX,subY)>0 Then tetWell(showX,showY)=tetShape(tetPiece,tetAngle,subX,subY)
        EndIf
       Next
      Next  
      If tetPieceY=>16
       PlaySound sFail : screenState=-1 : gotim=0
      Else
       If no_pieces>0
        GetNewPiece()
       Else
        PlaySound sMileStone : screenState=-2 : gotim=0 
       EndIf
      EndIf
     EndIf
	EndIf
	;identify lines 
	For y=1 To 16
	 liner=0
	 For x=1 To 10
      If tetWell(x,y)>0 Then liner=liner+1
     Next
     If liner=10
      PlaySound sCorrect
      tetSettleTim=5
      For x=1 To 10
       CreateExplosion(tetWellX(x),tetWellY(y))
       ScorePoint(tetWell(x,y))
       tetWell(x,y)=0
      Next
     EndIf
    Next
	;update well
	tetSettleTim=tetSettleTim-1
	If tetSettleTim<0 Or gotim<20 Then tetSettleTim=0
	If tetSettleTim=0 
	 For y=1 To 15
	  empty=1
	  For x=1 To 10
	   If tetWell(x,y)>0 Then empty=0
	  Next
      If empty=1
       For x=1 To 10
        tetWell(x,y)=tetWell(x,y+1) : tetWell(x,y+1)=0 
       Next
       tetSettleTim=5
      EndIf
     Next
    EndIf
    ;update score
    scoreTim=scoreTim-1
    If scoreTim<0 Then scoreTim=0
    gamNewScore(0)=0
    For cyc=1 To 6
     gamNewScore(0)=gamNewScore(0)+gamNewScore(cyc)
    Next
    If scoreTim=0
     For cyc=0 To 6
      If gamScore(cyc)<gamNewScore(cyc) Then gamScore(cyc)=gamScore(cyc)+1 : scoreTim=2
      If gamScore(cyc)>gamNewScore(cyc) Then gamScore(cyc)=gamScore(cyc)-1 : scoreTim=2
     Next
    EndIf
    ;If gamScore(0)=>gamMaxScore And screenState=1 Then PlaySound sMilestone : screenState=-2 : gotim=0

    ;EXPLOSIONS
	For v=1 To no_explodes
 	 If exState(v)>0
      exTim(v)=exTim(v)+1
      If exTim(v)>15 Then exState(v)=0
	  exY(v)=exY(v)+1
	 EndIf
	Next
	
 UpdateWorld
 Next
 RenderWorld 1

 ;DISPLAY
 DrawImage gBackground,rX#(400),rY#(300)
 ;red background
 If screenState=-1 And gotim<20
  Color Rnd(100,200),0,0
  Rect 0,0,GraphicsWidth(),GraphicsHeight(),1
 EndIf
 ;well
 ;For x=1 To 10
  ;For y=1 To 16
   ;DrawImage gWellBG,(tetWellX(x)-6)+(x*1),(tetWellY(y)+10)-(y*1) 
  ;Next
 ;Next
 If screenState<>0
  For x=1 To 10
   For y=1 To 16
    If tetWell(x,y)>0 Then DrawImage gIconLarge(tetWell(x,y)),tetWellX(x),tetWellY(y)
   Next
  Next
  ;piece
  If tetPiece>0 And screenState<>-2
    For subX=1 To 5
     For subY=1 To 5
      showX=tetPieceX+(subX-3)
      showY=tetPieceY+(subY-3)
      If showX=>1 And showX=<10 And showY=>1 And showY=<16
       If tetShape(tetPiece,tetAngle,subX,subY)>0 Then DrawImage gIconLarge(tetShape(tetPiece,tetAngle,subX,subY)),tetWellX(showX),tetWellY(showY) 
      EndIf
     Next
    Next
  EndIf
 EndIf
 ;preview next
 SetFont font(20)
 Outline("Next:",rX#(615),rY#(380)-200,0,0,0,255,240,0)
 If tetNextPiece>0
    x=rX#(615)-105 : y=rY#(380)
    If tetNextPiece=2 Then x=x+22 : y=y-25
    If tetNextPiece=3 Then x=x+17
    If tetNextPiece=7 Then y=y-25
    If gamDevelop=7 Then ConformPiece(tetNextPiece,tetNextType)
    For subX=1 To 5
     For subY=1 To 5
      If tetShape(tetNextPiece,1,subX,subY)>0
       DrawImage gIconLarge(tetShape(tetNextPiece,1,subX,subY)),x+(subX*35),y-(subY*35)
      EndIf
     Next
    Next 
    If gamDevelop=7 Then ConformPiece(tetPiece,tetType)
 EndIf
 SetFont font(10)
 Outline("("+no_pieces+" remaining)",rX#(615),rY#(380)-50,0,0,0,255,255,255)
 ;explosions
 For v=1 To no_explodes
  If exState(v)>0
   If exTim(v)=>1 And exTim(v)=<3 Then DrawImage gExplode,exX(v),exY(v),0
   If exTim(v)=>4 And exTim(v)=<6 Then DrawImage gExplode,exX(v),exY(v),1
   If exTim(v)=>7 And exTim(v)=<9 Then DrawImage gExplode,exX(v),exY(v),2
   If exTim(v)=>10 And exTim(v)=<12 Then DrawImage gExplode,exX(v),exY(v),3
  EndIf
 Next
 ;well overlay
 DrawImage gWell,rX#(400)-143,rY#(300)
 ;displays
 DisplayGameStatus(rX#(400)-140,rY#(300))
 DisplayGameChar(gamWorker,rX#(530),rY#(85))
 DisplayScore(gamScore(0),rX#(625),rY#(395))
 If game=1
  DrawProjectProfile(gamProject,rX#(470),rY#(415),0)
 EndIf
 ;cursor
 ;If foc<>oldfoc Then oldfoc=foc : PlaySound sMenuSelect  
 ;DrawImage gCursor,MouseX(),MouseY()

 Flip
 ;screenshot (F12)
 If KeyHit(88) Then Screenshot()

Wend
;leave
FreeTimer timer
;For count=0 To 6
 ;If gamScore(count)>gamMaxScore Then gamMaxScore=gamScore(count)
;Next
If game=1
 UpdateProject(gamProject)
 screen=50 : screenAgenda=4
Else
 screen=16
EndIf
End Function

;-----------------------------------------------------------------
;////////////////////// RELATED FUNCTIONS ////////////////////////
;-----------------------------------------------------------------

;GET NEW PIECE
Function GetNewPiece()
 no_pieces=no_pieces-1
 If tetNextPiece=0 Then tetNextPiece=Rnd(1,no_shapes)
 tetPiece=tetNextPiece : tetAngle=Rnd(1,4)
 tetPieceX=Rnd(3,8) : tetPieceY=16 : tetPieceOldY=17
 If no_pieces>0 Then tetNextPiece=Rnd(1,no_shapes) Else tetNextPiece=0
 ;conformity
 If gamDevelop=7
  tetType=tetNextType
  ConformPiece(tetPiece,tetType)
  If gamDevelop=>1 And gamDevelop=<6 Then tetNextType=gamDevelop Else tetNextType=Rnd(1,6) 
  ConformPiece(tetNextPiece,tetNextType)
 EndIf
End Function

;CONFORM PIECE
Function ConformPiece(n,style)
 For a=1 To 4
  For subX=1 To 5
   For subY=1 To 5
    If tetShape(n,a,subX,subY)>0 Then tetShape(n,a,subX,subY)=style
   Next
  Next
 Next
End Function

;CHECK SPILL OUT
Function SpillOut()
 value=0
 For subX=1 To 5
  For subY=1 To 5
   If tetShape(tetPiece,tetAngle,subX,subY)>0
    showX=tetPieceX+(subX-3) : showY=tetPieceY+(subY-3)
    If showX<1 Then value=1
    If showX>10 Then value=2
    If showY<1 Then value=3
   EndIf
  Next
 Next
 Return value
End Function