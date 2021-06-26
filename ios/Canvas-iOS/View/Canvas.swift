//
//  Canvas.swift
//  Canvas-iOS
//
//  Created by Lam Nguyen on 21/06/2021.
//

import UIKit
import SocketIO

class Canvas: UIView{
    
    var myPainting = Painting()
    var yourPaiting = Painting()

    override func draw(_ rect: CGRect) {
        super.draw(rect)
        
        guard let context = UIGraphicsGetCurrentContext() else{return}
        
        
        context.setLineWidth(10)
        context.setLineCap(.round)
        context.setStrokeColor(UIColor.getColor(hex: "black_20").cgColor) 
        
        yourPaiting.scanAllPoints(resolve: {(point, isNewStroke) in
            if (!isNewStroke){
                context.addLine(to: point)
            }else{
                context.move(to: point)
            }
        })
        
        
        context.strokePath()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        myPainting.addStroke(stroke: [CGPoint]())
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        if let point = touches.first?.location(in: nil){
            myPainting.addPointToLastStroke(point: point)
            WS.intance.socket.emit("device_data", myPainting.lastStrokeToJSONArray()!)
        }
    }
    
    
    
}
