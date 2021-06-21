const socket = require('socket.io')
const express = require('express')
const PORT = 3000

//set up server
const app = express();
const server = app.listen(PORT, ()=>{
	console.log(`Listening on port ${PORT}...`)
})

var conversation = [];
var room = "chatRoom"
//set up io
const io = socket(server)
io.on('connect', socket => {
	console.log("someone connected")
	console.log(socket.id) 
	// let percent = 10;

	// let timer = setInterval(()=>{
	// 	// console.log(percent)
	// 	if (percent > 100){
	// 		// socket.emit('end')
	// 		clearInterval(timer)
	// 	}else{
	// 		socket.emit('percent', percent);
	// 	}
	// 	percent += 1;
	// }, 20)

	socket.on('join', () => {
		console.log(`${socket.id} has just joined room`);
		socket.join(room);

	})

	socket.on('sendData', (mess) =>{
		// conversation.push(mess)
		// console.log(conversation)

		//emit
		socket.to(room).emit('new msg', mess);

		console.log("---------------------------")
	})

	socket.on('disconnect', (socket) =>{
		console.log("User left");
	})

});
 
