import React, { Component } from "react";
import { Button, TextField } from "@material-ui/core";
import "bootstrap/dist/css/bootstrap.min.css";
import * as service from "../../service/ajax";

class Notice extends Component {
  constructor({ match, history }) {
    super();
    this.state = {
      id: match.params.id,
      data: {
        id: 0,
        title: "",
        content: "",
        writer: {
          id: 0,
          email: "",
        },
        createDateTime: "",
        lastModifiedDateTime: "",
      },
      buttonReadOnly: {
        modifying: true,
        saving: true,
      },
      inputReadOnly: true,
      create: false,
      titleValue: "",
      contentValue: "",
      history: history,
      progress: false,
      formData: null,
    };
  }

  componentDidMount() {
    this.startProgress();
    if (this.state.id !== undefined) {
      this.readData();
      return;
    }
    this.createData();
  }

  readData() {
    service.findNotice(this.state.id).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        if (data !== undefined) {
          this.setState({
            id: data.id,
            data: data,
            create: false,
            buttonReadOnly: {
              modifying: !data.editable,
              saving: true,
            },
            inputReadOnly: true,
            titleValue: data.title,
            contentValue: data.content,
          });
        }
        return;
      }
      alert(result.status + " - 오류 메시지 : " + result.data.errorMessage);
      this.state.history.push("/");
    });
  }

  createData() {
    this.setState({
      id: this.state.id,
      data: this.state.data,
      create: true,
      buttonReadOnly: {
        modifying: true,
        saving: false,
      },
      inputReadOnly: false,
      titleValue: "",
      contentValue: "",
    });
  }

  startProgress() {
    this.setState({
      progress: true,
    });
  }

  endProgress() {
    this.setState({
      progress: false,
    });
  }

  render() {
    const changeReadOnlyStatus = (
      modifyingButtonReadOnly,
      savingButtonReadOnly,
      inputReadOnly
    ) => {
      this.setState({
        buttonReadOnly: {
          modifying: modifyingButtonReadOnly,
          saving: savingButtonReadOnly,
        },
        inputReadOnly: inputReadOnly,
      });
    };

    const eventSaveClick = () => {
      if (
        this.state.titleValue === undefined ||
        this.state.titleValue === null ||
        this.state.titleValue === ""
      ) {
        alert("제목은 필수 입력 항목입니다.");
        return;
      }
      if (
        this.state.contentValue === undefined ||
        this.state.contentValue === null ||
        this.state.contentValue === ""
      ) {
        alert("내용은 필수 입력 항목입니다.");
        return;
      }
      const saveData = {
        title: this.state.titleValue,
        content: this.state.contentValue,
      };
      // let formData = new FormData();
      // let files = document.getElementById("file").files;
      // for (var i = 0; i < files.length; i++) {
      //   formData.append("file", files[i]);
      // }

      if (this.state.create) {
        // service.saveFiles(formData).then((resule) => {
        // });
        saveService(saveData);
        return;
      }
      updateService(this.state.id, saveData);
    };

    const saveService = (data) => {
      service.saveNotice(data).then((result) => {
        if (result.status === 201) {
          alert("정상 처리되었습니다.");
          this.state.history.push("/");
          return;
        }
        alert(result.status + " - 오류 메시지 : " + result.data.errorMessage);
      });
    };

    const updateService = (id, data) => {
      if (!window.confirm("수정하시겠습니까?")) {
        return;
      }
      service.updateNotice(id, data).then((result) => {
        if (result.status === 200) {
          alert("정상 처리되었습니다.");
          const data = result.data;
          if (data !== undefined) {
            this.setState({
              id: data.id,
              data: data,
              create: false,
              buttonReadOnly: {
                modifying: !data.editable,
                saving: true,
              },
              inputReadOnly: true,
              titleValue: data.title,
              contentValue: data.content,
            });
          }
          changeReadOnlyStatus(false, true, true);
          return;
        }
        alert(result.status + " - 오류 메시지 : " + result.data.errorMessage);
      });
    };

    const eventModifyClick = () => {
      changeReadOnlyStatus(true, false, false);
    };

    const eventDeleteClick = () => {
      if (!window.confirm("삭제하시겠습니까?")) {
        return;
      }
      service.deleteNotice(this.state.id).then((result) => {
        if (result.status === 204) {
          alert("정상 처리되었습니다.");
          this.state.history.push("/");
          return;
        }
        alert(result.status + " - 오류 메시지 : " + result.data.errorMessage);
      });
    };

    const onChangTitle = (e) => {
      this.setState({
        titleValue: e.target.value,
      });
    };

    const onChangContent = (e) => {
      this.setState({
        contentValue: e.target.value,
      });
    };

    // const onChangFiles = (e) => {};

    return (
      <div>
        <div className='mb-3 row'>
          <label for='title' className='col-sm-1 col-form-label'>
            TITLE
          </label>
          <div className='col-sm-11'>
            <TextField
              id='title'
              size='small'
              value={this.state.titleValue}
              InputProps={{
                readOnly: this.state.inputReadOnly,
              }}
              onChange={onChangTitle}
              fullWidth={true}
              variant='outlined'
            />
          </div>
        </div>
        <div className='mb-3 row' hidden={this.state.create}>
          <div>
            <label for='writer' className='col-form-label'>
              WRITER :{" "}
              <b>{this.state.create ? "" : this.state.data.writer.email}</b>
            </label>
          </div>
          <div>
            <label for='createDate' className='col-form-label text-end'>
              CREATE :{" "}
              <b>{this.state.create ? "" : this.state.data.createDateTime}</b>
            </label>
          </div>
          <div>
            <label for='lastModifiedDate' className='col-form-label text-end'>
              LAST UPDATE :{" "}
              <b>
                {this.state.create ? "" : this.state.data.lastModifiedDateTime}
              </b>
            </label>
          </div>
        </div>

        <div className='mb-3 row'>
          <label for='content' className='col-sm-2 col-form-label'>
            CONTENT
          </label>
          <TextField
            id='content'
            multiline
            rows={10}
            fullWidth={true}
            variant='outlined'
            value={this.state.contentValue}
            onChange={onChangContent}
            InputProps={{
              readOnly: this.state.inputReadOnly,
            }}
          />
        </div>
        {/* <div className='mb-3'>
          <input
            className='form-control'
            type='file'
            id='file'
            onChange={onChangFiles}
            multiple
          />
        </div> */}
        <div>
          <span>
            <Button href='/' variant='outlined'>
              List
            </Button>
          </span>
          <span>
            <Button
              variant='outlined'
              onClick={eventSaveClick}
              disabled={this.state.buttonReadOnly.saving}
            >
              Save
            </Button>
          </span>
          <span>
            <Button
              variant='outlined'
              onClick={eventModifyClick}
              hidden={this.state.buttonReadOnly.modifying && this.state.create}
              disabled={this.state.buttonReadOnly.modifying}
            >
              Modify
            </Button>
          </span>
          <span>
            <Button
              variant='outlined'
              onClick={eventDeleteClick}
              hidden={this.state.buttonReadOnly.modifying && this.state.create}
              disabled={this.state.buttonReadOnly.modifying}
            >
              Delete
            </Button>
          </span>
        </div>
      </div>
    );
  }
}

export default Notice;
