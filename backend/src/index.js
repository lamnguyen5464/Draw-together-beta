const socket = require("socket.io");
const express = require("express");
const lodash = require("lodash");
const PORT = 3000;

//set up server
const app = express();
const server = app.listen(PORT, () => {
     console.log(`Listening on port ${PORT}...`);
});


const usersMap = {};
const rooms = {};

const log = () => {
     console.log("@@@ LOG: ", JSON.stringify({
          usersMap,
          rooms,
     }, null, 3));
}

var room = "chatRoom";
//set up io
const io = socket(server);
io.on("connect", (socket) => {
     // console.log(socket.id, "connected");

     socket.on("request_join", (room) => {
          console.log(`${socket.id} requests to join ${room}`)

          if (!rooms[room]) {
               rooms[room] = [socket.id]
          } else {
               rooms[room].push(socket.id);
          }
          usersMap[socket.id] = room
          // log()

          socket.join(room)
     });


     const handleDataChange = lodash.debounce((res) => {
          socket.to(usersMap[socket.id]).emit("server_data", JSON.stringify(res));
          socket.emit("server_data", JSON.stringify(res));
     }, 30)

     socket.on("device_data", handleDataChange)

     // socket.on("on_submit", (res) => {
     //      console.log('here');
     //      debounceEmitter(socket, res)
     // })

     socket.on("disconnect", (socket) => {
          console.log("User left");
     });
});
