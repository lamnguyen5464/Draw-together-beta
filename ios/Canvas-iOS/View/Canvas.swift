//
//  Canvas.swift
//  Canvas-iOS
//
//  Created by Lam Nguyen on 21/06/2021.
//

import UIKit

class Canvas: UIView{
    
    var lines = [[CGPoint]]()
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        
        guard let context = UIGraphicsGetCurrentContext() else{return}
        
        print("Start draw")
        
        context.setLineWidth(15)
        context.setLineCap(.round)
        context.setStrokeColor(UIColor.getColor(hex: "black_09").cgColor) 
        
        
        for (_, line) in lines.enumerated(){
            for(j, point) in line.enumerated(){
                print(point.x, point.y)
                if ((j) != 0){
                    context.addLine(to: point)
                }else{
                    context.move(to: point)
                }
            }
        }
        
        
        context.strokePath()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        lines.append([CGPoint]())
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        if let point = touches.first?.location(in: nil){
            
            guard var lastLine = lines.popLast() else {return}
            
            
           
            
            lastLine.append(point)
            lines.append(lastLine)
            
            setNeedsDisplay()
            
        }
    }
    
    
    
}
