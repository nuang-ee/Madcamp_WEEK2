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
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        res.json(user.claimee.filter(e => e.received === false))
      } else {
        res.json({
          message: "no user there"
        })
      }
    }
  });
}

exports.addClaimee = (req, res) => {
  const {
    uid,
    claimee_uid,
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
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      // new Claimer list
      if (user) {
        const newClaimee = new claimeeModel();
        newClaimee.claimee_uid = claimee_uid;
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
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
}

exports.receivedAmount = (req, res) => {
  const {
    uid,
    _id
  } = req.body
  let sender, date;
  userModel.find({
    uid
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        user.claimee.map(
          e => {
            if (JSON.stringify(e._id) === JSON.stringify(ObjectId(_id))) {
              if (e.sent) {
                e.received = true;
                sender = e.claimee_uid;
                date = e.date;
              } else {
                res.json({
                  result: 0
                })
                return;
              }
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
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  });
  userModel.find({
    sender
  }, (err, [user]) => {
    if (err) {
      console.error(err);
      res.status(500).send({
        message: "Something wrong"
      });
    } else {
      if (user) {
        user.claimer.map(e => {
          if (e.date === date && e.sent) {
            e.received = true
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
      } else {
        res.json({
          message: "no user here"
        })
      }
    }
  })
}