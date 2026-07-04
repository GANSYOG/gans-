const express = require('express');
const router = express.Router();

const bookings = [];

router.post('/', (req, res) => {
    const { bikeId, customerId, hours, totalAmount } = req.body;
    const newBooking = { 
        id: Date.now(), 
        bikeId, 
        customerId, 
        hours, 
        totalAmount,
        status: 'Active', 
        startTime: new Date() 
    };
    bookings.push(newBooking);
    res.status(201).json(newBooking);
});

router.get('/:customerId', (req, res) => {
    res.json(bookings.filter(b => b.customerId == req.params.customerId));
});

module.exports = router;
