//
//  ViewController.swift
//  Canvas-iOS
//
//  Created by Lam Nguyen on 21/06/2021.
//

import UIKit
import Foundation
import SocketIO

class ViewController: UIViewController {
    
    let canvas = Canvas()
    
    override func loadView(){
        self.view = canvas
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        WS.intance.connectSocket()
        
        
        WS.intance.setEventListener(eventName: "server_data", resolve: {(data) in
            let dataString = data as? String ?? ""
            
            self.canvas.yourPaiting.addStroke(stringStroke: dataString)
            self.canvas.setNeedsDisplay()
        })
        
        canvas.backgroundColor = UIColor.getColor(hex: "baby_blue")

        
    }
    
    
}

