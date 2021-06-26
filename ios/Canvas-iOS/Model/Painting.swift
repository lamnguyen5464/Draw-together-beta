//
//  Painting.swift
//  Canvas-iOS
//
//  Created by Lam Nguyen on 26/06/2021.
//

import Foundation
import UIKit

class Painting{
    var listStrokes =  [[CGPoint]]()
    
    init(){
        
    }
    
    init(dataString: String){
        if let jsonArray = try? JSONSerialization.jsonObject(with: Data(dataString.utf8), options: []) as? [[Any]] {
            for(_, jsonStroke) in jsonArray.enumerated(){
                addStroke(stroke: [CGPoint]())
                for(_, stringPoint) in jsonStroke.enumerated(){
                    let dicPoint = stringPoint as? NSDictionary
                    addPointToLastStroke(point: CGPoint(x: dicPoint?["x"] as? CGFloat ?? 0, y: dicPoint?["y"] as? CGFloat ?? 0))
                }
            }
        }
        
    }
    
    func addStroke(stringStroke: String){
        if let jsonStroke = try? JSONSerialization.jsonObject(with: Data(stringStroke.utf8), options: []) as? [Any] {
            addStroke(stroke: [CGPoint]())
            for(_, stringPoint) in jsonStroke.enumerated(){
                let dicPoint = stringPoint as? NSDictionary
                addPointToLastStroke(point: CGPoint(x: dicPoint?["x"] as? CGFloat ?? 0, y: dicPoint?["y"] as? CGFloat ?? 0))
            }
            
        }
        
    }
    
    func addStroke(stroke: [CGPoint]){
        listStrokes.append(stroke)
    }
    
    func addPointToLastStroke(point: CGPoint){
        guard var lastStroke = listStrokes.popLast() else {return}
        
        lastStroke.append(point)
        listStrokes.append(lastStroke)
    }
    
    func scanAllPoints(resolve: (CGPoint, Bool) -> Void) {
        for (_, line) in listStrokes.enumerated(){
            for(j, point) in line.enumerated(){ 
                resolve(point, j == 0)
            }
        }
    }
    
    func lastStrokeToJSONArray() -> String? {
        var dicArray = [NSDictionary]()
        
        if (listStrokes.count != 0 && listStrokes.last?.count != 0){
            for (_, point) in listStrokes.last!.enumerated(){
                let dic = ["x" : point.x, "y": point.y]
                dicArray.append(dic as NSDictionary)
            }
        }
        guard let jsonData = try? JSONSerialization.data(withJSONObject: dicArray, options: []) else { return nil }
        return String(data: jsonData, encoding: String.Encoding.ascii)
        
    }
}
