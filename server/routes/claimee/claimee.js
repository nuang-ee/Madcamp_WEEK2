const claimeeModel = require('../../models/claimee');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimee = (req, res) => {
  const {
    uid
  } = req.body
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(e);
    } else {
      res.json(user.claimee.filter(e => e.received === false && e.sent === false))
    }
  });
}

exports.addClaim = (req, res) => {
  const {
    uid,
    claimee,
    amount,
    name,
    date
  } = req.body;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(err);
    } else {
      // new Claimer list
      const newClaimee = new claimeeModel();
      newClaimee.claimee = claimee;
      newClaimee.amount = amount;
      newClaimee.name = name;
      newClaimee.date = date;
      user.claimee.push(newClaimee)
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
        } else {
          res.json({
            _id: newClaimee._id,
            result: 1
          });
        }
      });
    }
  })
}

exports.sendAmount = (req, res) => {
  const {
    uid,
    _id
  } = req.body;
  let receiver;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(err);
    } else {
      user.claimer.map(
        e => {
          console.log(e)
          if (JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))) {
            e.sent = true
            receiver = e.claimer
          }
        }
      )
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
          return;
        }
      })
    }
  })
  userModel.find({
    receiver,
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send(err);
    } else {
      user.claimee.map(e => {
        console.log(e.claimee, uid)
        console.log(typeof e.claimee, typeof uid)
        if (JSON.stringify(ObjectId(e.claimee)) === JSON.stringify(uid)) {
          e.sent = true
        }
      })
      user.save(err => {
        if (err) {
          console.error(err);
          res.json({
            result: 0
          });
        } else {
          res.json({
            result: 1
          });
        }
      })
    }
  })
}