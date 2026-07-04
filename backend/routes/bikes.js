const express = require('express');
const router = express.Router();

const bikes = [
    { id: 1, brand: 'Royal Enfield', model: 'Classic 350', pricePerHour: 50, location: 'Downtown Hub', type: 'Gear' },
    { id: 2, brand: 'Ather', model: '450X', pricePerHour: 40, location: 'Tech Park', type: 'Electric' }
];

router.get('/', (req, res) => {
    res.json(bikes);
});

router.post('/', (req, res) => {
    // Requires Admin or Bike Owner token middleware
    const newBike = { id: Date.now(), ...req.body };
    bikes.push(newBike);
    res.status(201).json(newBike);
});

router.get('/:id', (req, res) => {
    const bike = bikes.find(b => b.id == req.params.id);
    bike ? res.json(bike) : res.status(404).json({ message: 'Bike not found' });
});

module.exports = router;
