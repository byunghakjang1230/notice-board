import React, { Component } from "react";
import { BrowserRouter as Router } from "react-router-dom";
import "../css/Board.css";
import Navigae from "./Navigate";
import BasicTable from "./BasicTable";
import Pagination from "./Pagination";
import * as service from "../../service/ajax";

const DEFAULT_PAGE_NUMBER = 0;
const DEFAULT_PAGE_SIZE = 5;

const createData = (id, writer, title, createDateTime) => {
  return { id, writer, title, createDateTime };
};

class Board extends Component {
  constructor(props) {
    super();
    this.state = {
      data: [],
      currentPage: DEFAULT_PAGE_NUMBER,
      totalPages: 0,
      history: props.history,
    };
  }

  componentDidMount() {
    const defaultData = service.getPagingNotices(DEFAULT_PAGE_NUMBER);
    defaultData.then((result) => {
      if (result.status === 200) {
        const data = result.data;
        this.setState({
          data: data.content.map((content) => {
            return createData(
              content.id,
              content.writer.email,
              content.title,
              content.createDateTime
            );
          }),
          currentPage: data.number,
          totalPages: data.totalPages,
        });
        return;
      }
      alert(result.status + " - 오류 메시지 : " + result.data.errorMessage);
    });
  }

  render() {
    const history = this.state.history;
    const data = this.state.data;

    const handlePageChange = (page) => {
      service.getPagingNotices(page).then((value) => {
        const data = value.data;
        this.setState({
          data: data.content.map((content) => {
            return createData(
              content.id,
              content.writer.email,
              content.title,
              content.createDateTime
            );
          }),
          currentPage: data.number,
          totalPages: data.totalPages,
        });
      });
    };

    return (
      <div>
        <Router>
          <Navigae />
          <BasicTable history={history} data={data} />
          <Pagination
            totalPages={this.state.totalPages}
            currentPage={this.state.currentPage}
            pageSize={DEFAULT_PAGE_SIZE}
            onPageChange={handlePageChange}
          />
        </Router>
      </div>
    );
  }
}

export default Board;
