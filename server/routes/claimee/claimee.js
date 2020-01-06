const claimeeModel = require('../../models/claimee');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimer = (req, res) => {
  // send == true인 경우에는 출력 안해야 함
  const { uid } = req.body
  userModel.find({ uid }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      res.json(user.claimee.filter(e => e.received === false))
    }
  });
  res.end();
}

exports.addClaim = (req, res) => {
  res.end();
}

exports.sendAmount = (req, res) => {
  res.end();
}