const express = require('express');
const cors = require('cors');
const http = require('http');
const { Server } = require('socket.io');
const mongoose = require('mongoose');
require('dotenv').config();

const app = express();
const server = http.createServer(app);
// WebSocket for Live Tracking and Chat
const io = new Server(server, { cors: { origin: '*' } });

app.use(cors());
app.use(express.json());

// MongoDB connection
mongoose.connect(process.env.MONGO_URI || 'mongodb://localhost:27017/rentyourbike', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
}).then(() => console.log('MongoDB Connected to Production Layer'))
  .catch(err => console.log('MongoDB Connection Error:', err));

// Register API Routes
app.use('/api/auth', require('./routes/auth'));
app.use('/api/bikes', require('./routes/bikes'));
app.use('/api/bookings', require('./routes/bookings'));

// WebSockets (Live GPS Tracking)
io.on('connection', (socket) => {
  console.log('User connected to socket:', socket.id);
  
  // Real-time GPS location updates for rides
  socket.on('join_ride', (rideId) => socket.join(`ride_${rideId}`));
  socket.on('location_update', (data) => {
      io.to(`ride_${data.rideId}`).emit('new_location', { lat: data.lat, lng: data.lng });
  });

  // Real-time Chat
  socket.on('send_message', (data) => {
      io.to(`ride_${data.rideId}`).emit('receive_message', data);
  });
  
  socket.on('disconnect', () => console.log('User disconnected:', socket.id));
});

const PORT = process.env.PORT || 5000;
server.listen(PORT, () => console.log(`🚀 Production Backend running on port ${PORT}`));
