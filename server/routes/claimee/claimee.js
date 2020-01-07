const claimeeModel = require('../../models/claimee');
const userModel = require('../../models/user');
const ObjectId = require('mongodb').ObjectId


exports.getClaimee = (req, res) => {
  const {
    uid
  } = req.body
  console.log("getClaimee>>",
    req.body)
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
        const tmp = user.claimee.filter(e => e.received === false)
        const done = tmp.map(e => ({
          _id: e._id,
          claimee: e.claimee,
          amount: e.amount,
          name: e.name,
          date: e.date,
          sent: e.sent,
          received: e.received
        }))
        res.json(done)
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
      return;
    } else {
      if (user) {
        let claimee_uid;
        userModel.find({
          name: claimee
        }, (err, [target]) => {
          if (err) {
            console.error(err);
            res.status(500).send({
              message: "Something wrong"
            });
            return;
          } else {
            // new Claimer list
            if (target) {
              claimee_uid = target.uid
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
                    uid: claimee_uid,
                    result: 1
                  });
                }
              });
            } else {
              res.json({
                message: "no target here"
              })
            }

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