// generateToken.ts

import * as jwt from 'jsonwebtoken';

const payload = {
  sub: 'test@mail.com',
  auth: 'ROLE_USER',
  exp: Math.floor(Date.now() / 1000) + (60 * 60 * 24 * 365 * 10) // 10 years
};

const secret = 'UH5xU2.}R_o=cO~,4%0WDEVkUN!DCj2kKc,'; // Use your actual secret
const token = jwt.sign(payload, secret);

console.log('Generated JWT Token:', token);
