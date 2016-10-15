import React, { PropTypes } from 'react';
import { Upload, Icon, Row, Col, Button, Tooltip  } from 'antd';

const UploadBox = React.createClass({

  getInitialState() {
    return {
      fileList: [],
      notUploaded: 0,
      uploading: 1,
      uploaded: 2,
      uploadError: 3,
      fileNumber: undefined,
    }
  },

  componentWillMount() {

  },

  componentDidMount() {

  },

  componentWillReceiveProps(nextProps) {
    let fileList = nextProps.fileList || [];
    this.setState({
      fileList: fileList.map(item => {
        return item;
      }),
      fileNumber: nextProps.fileNumber
    })
  },

  onSuccess() {

  },

  onError() {

  },

  getFileById(id) {
    const fileList = this.state.fileList;
    for (let i in fileList) {
      if (fileList[i].id == id) {
        return fileList[i]
      }
    }
    return false
  },

  getFileIndexByFile(file, fileList) {
    const id = file.id
    for (let i in fileList) {
      if (fileList[i].id == id) {
        return i
      }
    }
    return false
  },

  renderUpload(file, fileList) {
    return {
      file,
      fileList,
      replace: (f, list) => {
        const index = this.getFileIndexByFile(f, list);
        list[index] = f;
        return list
      }
    }
  },

  renderByStatus(status, id) {
    const { upload } = this.props;
    switch (status) {
      case 0:
        return (
          <Tooltip placement="top" title={'已上传'}>
            <a href="javascript:;">
              <Icon type="minus-circle-o" style={{ color: 'green' }}/>
            </a>
          </Tooltip>
        )
      case 1:
        return (
          <Tooltip placement="top" title={'上传中'}>
            <a href="javascript:;">
              <Icon type="loading" style={{ color: 'green' }}/>
            </a>
          </Tooltip>
        )
      case 2:
        return (
          <Tooltip placement="top" title={'上传成功'}>
            <a href="javascript:;">
              <Icon type="check-circle-o" style={{ color: 'green' }}/>
            </a>
          </Tooltip>
        )
      default:
        return (
          <Tooltip placement="top" title={'点击重新上传'}>
            <a href="javascript:;" onClick={() => {
              let current = this.getFileById(id);
              current.status = this.state.uploading;
              upload(this.renderUpload(current, this.state.fileList));
            }}><Icon type="exclamation-circle-o" style={{ color: 'red' }}/></a>
          </Tooltip>
        )

    }
  },

  render() {
    const { upload } = this.props;
    let fileList = this.state.fileList || [];
    const fileNumber = this.state.fileNumber;
    return (
      <div>
        <Upload
          {...this.props}
          showUploadList={ false }
          fileList={fileList}
          beforeUpload={file => {
            var reader = new FileReader();
            let list = [];
            reader.onloadend = (e) => {
              if (fileNumber) {
                if (fileList.length >= fileNumber) {
                  fileList = fileList.slice(1);
                }
              }
              const current = {
                type: file.type,
                status: this.state.uploading,
                id: file.uid,
                fileData: file
              }
              fileList.push(current)
              this.setState({
                fileList
              })
              upload(this.renderUpload(current, fileList));
            }
            reader.readAsBinaryString(file);
            return false
        }}
        >
        <Button type="ghost">
          <Icon type="upload" /> 点击上传
        </Button>
        </Upload>
        {
      fileList.map((item, index) => {
        return (
          <Row key={index}>
            <Col>
              <Icon type="file" />
              &nbsp; &nbsp;
              <a href="">{item.id}</a>
              &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
              {
                this.renderByStatus(item.status, item.id)
              }
            </Col>
          </Row>
        )
      })
    }
      </div >
    );
  }
})


export {
UploadBox as default,
}

