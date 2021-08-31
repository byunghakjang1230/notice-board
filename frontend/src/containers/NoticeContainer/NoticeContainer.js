import React, { Component } from "react";
import { BrowserRouter, Link, Route, Switch } from "react-router-dom";
import { NoticeWrapper, Board, Join, Login, Notice } from "../../components";

class NoticeContainer extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <NoticeWrapper>
          <BrowserRouter>
            <Switch>
              <Route exact path='/' component={Board} />
              <Route exact path='/notice/:id' component={Notice} />
              <Route exact path='/notice' component={Notice} />
              <Route exact path='/join' component={Join} />
              <Route exact path='/login' component={Login} />
            </Switch>
          </BrowserRouter>
        </NoticeWrapper>
      </div>
    );
  }
}

export default NoticeContainer;
