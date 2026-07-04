const express = require('express');
const router = express.Router();
const jwt = require('jsonwebtoken');

// In-memory array used for immediate execution template, replace with Mongoose User Schema for Prod
const users = [];

router.post('/register', async (req, res) => {
    const { name, email, password, role } = req.body;
    // In prod: await bcrypt.hash(password, 10);
    const newUser = { id: Date.now(), name, email, role, password };
    users.push(newUser);
    res.status(201).json({ message: 'User registered successfully', userId: newUser.id });
});

router.post('/login', async (req, res) => {
    const { email, password } = req.body;
    const user = users.find(u => u.email === email && u.password === password);
    
    if (!user) return res.status(401).json({ message: 'Invalid credentials' });
    
    const token = jwt.sign(
        { id: user.id, role: user.role }, 
        process.env.JWT_SECRET || 'ryb_super_secret', 
        { expiresIn: '7d' }
    );
    
    res.json({ token, user: { id: user.id, name: user.name, email: user.email, role: user.role } });
});

module.exports = router;
