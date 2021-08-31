import React, { useState } from "react";
import * as service from "../../service/ajax";
import { makeStyles } from "@material-ui/core/styles";
import { TextField, Button } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  root: {
    "& > *": {
      margin: theme.spacing(1),
      width: "25ch",
    },
  },
}));

const Join = ({ history }) => {
  const classes = useStyles();

  const joinState = useState({
    email: "",
    password: "",
    confirmpw: "",
  });

  const [join, setJoin] = joinState;

  const onClickJoin = () => {
    if (join.email === "") {
      alert("이메일을 입력해주세요.");
      return;
    }
    if (join.password === "") {
      alert("비밀번호를 입력해주세요.");
      return;
    }
    if (join.password !== join.confirmpw) {
      alert("비밀번호가 다릅니다.");
      return;
    }
    const joinData = { email: join.email, password: join.password };
    const data = service.join(joinData);
    data.then((value) => {
      history.push("/login");
    });
  };
  const onChangeEmail = (e) => {
    setJoin({
      email: e.target.value,
      password: join.password,
      confirmpw: join.confirmpw,
    });
  };
  const onChangePassword = (e) => {
    setJoin({
      email: join.email,
      password: e.target.value,
      confirmpw: join.confirmpw,
    });
  };
  const onChangeConfirmpw = (e) => {
    setJoin({
      email: join.email,
      password: join.password,
      confirmpw: e.target.value,
    });
  };
  return (
    <>
      <form className={classes.root} noValidate autoComplete='off'>
        <div>
          <TextField
            id='email'
            label='Email'
            type='email'
            onChange={onChangeEmail}
          />
        </div>
        <div>
          <TextField
            id='password'
            label='Password'
            type='password'
            onChange={onChangePassword}
          />
          <TextField
            id='confirmps'
            label='Confirm Password'
            type='password'
            error={join.password !== join.confirmpw}
            onChange={onChangeConfirmpw}
          />
        </div>
      </form>
      <Button variant='contained' onClick={onClickJoin}>
        Join
      </Button>
    </>
  );
};

export default Join;
