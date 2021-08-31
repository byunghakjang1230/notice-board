import React, { useState } from "react";
import * as service from "../../service/ajax";
import * as loginService from "../../service/login";
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

const Login = ({ history }) => {
  const loginState = useState({
    email: "",
    password: "",
  });

  const [login, setLogin] = loginState;

  const onClickLogin = () => {
    if (login.email === "") {
      alert("이메일을 입력해주세요.");
      return;
    }
    if (login.password === "") {
      alert("비밀번호를 입력해주세요.");
      return;
    }
    const data = service.login(login);
    data.then((value) => {
      console.log(value.data);
      loginService.setLoginToken(value.data.token);
      history.push("/");
    });
  };
  const onChangeEmail = (e) => {
    setLogin({ email: e.target.value, password: login.password });
  };
  const onChangePassword = (e) => {
    setLogin({ email: login.email, password: e.target.value });
  };
  const classes = useStyles();
  return (
    <>
      <form className={classes.root} noValidate autoComplete='off'>
        <TextField
          id='email'
          label='Email'
          type='email'
          onChange={onChangeEmail}
        />
        <TextField
          id='password'
          label='Password'
          type='password'
          onChange={onChangePassword}
        />
      </form>
      <Button variant='contained' onClick={onClickLogin}>
        Login
      </Button>
    </>
  );
};

export default Login;
