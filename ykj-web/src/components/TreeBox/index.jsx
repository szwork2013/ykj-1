import React, { PropTypes } from 'react';
import { Tree, Modal, Input, Icon } from 'antd';

import Box from '../Box';

const TreeNode = Tree.TreeNode;

const AsynTreeBox = () => {

  let treeDataState = [
    { name: 'pNode 01', key: '0-0' },
    { name: 'pNode 02', key: '0-1' },
    { name: 'pNode 03', key: '0-2', isLeaf: true },
  ];
  
  const onSelect = function(info) {
    console.log('selected', info);
  }
  const onCheck = function(info) {
    console.log('onCheck', info);
  }

  const generateTreeNodes = function(treeNode) {
    const arr = [];
    const key = treeNode.props.eventKey;
    for (let i = 0; i < 3; i++) {
      arr.push({ name: `leaf ${key}-${i}`, key: `${key}-${i}` });
    }
    return arr;
  }

  const setLeaf = function(treeData, curKey, level) {
    const loopLeaf = (data, lev) => {
      const l = lev - 1;
      data.forEach((item) => {
        if ((item.key.length > curKey.length) ? item.key.indexOf(curKey) !== 0 :
          curKey.indexOf(item.key) !== 0) {
          return;
        }
        if (item.children) {
          loopLeaf(item.children, l);
        } else if (l < 1) {
          item.isLeaf = true;
        }
      });
    };
    loopLeaf(treeData, level + 1);
  }

  const getNewTreeData = function(treeData, curKey, child, level) {
    const loop = (data) => {
      if (level < 1 || curKey.length - 3 > level * 2) return;
      data.forEach((item) => {
        if (curKey.indexOf(item.key) === 0) {
          if (item.children) {
            loop(item.children);
          } else {
            item.children = child;
          }
        }
      });
    };
    loop(treeData);
    setLeaf(treeData, curKey, level);
  }

  const onLoadData = function(treeNode) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const treeData = [...treeDataState];
        getNewTreeData(treeData, treeNode.props.eventKey, generateTreeNodes(treeNode), 2);
        treeDataState = treeData;
        resolve();
      }, 1000);
    });
  }

  const renderTreeNodes = function() {
    const loop = data => data.map((item) => {
      if (item.children) {
        return <TreeNode title={item.name} key={item.key}>{loop(item.children)}</TreeNode>;
      }
      return <TreeNode title={item.name} key={item.key} isLeaf={item.isLeaf} disabled={item.key === '0-0-0'} />;
    });
    const treeNodes = loop(treeDataState);
    return treeNodes;
  }

  return (
    <Tree onSelect={onSelect} loadData={onLoadData}>
      {renderTreeNodes()}
    </Tree>
  )
}

AsynTreeBox.propTypes = {

}

AsynTreeBox.defaultProps = {
  
}

/******************************Tree************************************/

const MyTree = React.createClass({

  getInitialState() {
    return {
      treeData: [],
      expandedKeys: [],
      autoExpandParent: true,
      checkedKeys: [],
      selectedKeys: [],
      multiple: true,
      checkable: true,
      data: [],
    }
  },

  componentWillMount() {
    
  },
    
  componentDidMount() {
    const treeData =  this.props.treeData;
    const data = this.setChildrenNode(treeData);


    setTimeout(() => {
      this.setState({
        treeData,
        data,
        multiple: this.props.multiple,
        checkable: this.props.checkable,
      });
    }, 100);
  },

  componentWillReceiveProps(nextProps) {
    
  },

  clear() {
    
  },

  onExpand(expandedKeys) {
    this.setState({
      expandedKeys
    })
  },

  onCheck(checkedKeys) {
    this.setState({
      checkedKeys
    })
  },

  onSelect(selectedKeys, info) {
    this.props.onSelect(selectedKeys);
    this.setState({
      selectedKeys
    })
  },

  loop(data) {
    return data.map((item) => {
      if (item.children) {
        return (
          <TreeNode key={item.id} title={item.name}>
            {this.loop(item.children)}
          </TreeNode>
        );
      }
      return <TreeNode key={item.id} title={item.name} />;
    });
  },

  getNodeByPid(pId, nodes) {
    for (let i in nodes) {
      if (pId == nodes[i].id) {
        return i
      }
    }
  },

  setChildrenNode(data) {
    let nodes = data;
    for (let i in nodes) {
      if (nodes[i].pId) {
        nodes[i].flag = true;
        let index = this.getNodeByPid(nodes[i].pId, data);
        let children = nodes[index].children || [];
        children.push(nodes[i])
        nodes[index].children = children;
      }
    }
    return nodes.filter(item => item.flag !== true);
  },

  render() {
    const { defaultValue } = this.props;
    return (
      <Tree
        expandedKeys={this.state.expandedKeys}
        checkedKeys={this.state.checkedKeys}
        selectedKeys={this.state.selectedKeys.length > 0 ? this.state.selectedKeys : [defaultValue]}
        autoExpandParent={this.state.autoExpandParent}
        onCheck={this.onCheck} 
        onSelect={this.onSelect} 
        onExpand={this.onExpand}
        multiple={ this.state.multiple }
        checkable={ this.state.checkable }
      >
        {
          this.loop(this.setChildrenNode(this.state.data))
        }
      </Tree>
    );
  }
})

/**********************************TreeBox*************************************/

const TreeBox = React.createClass({

  getInitialState() {
    return {
      showTreeBox: false,
      treeData: [
      ],
      value: undefined,
      selectingValue: undefined,
    }
  },

  componentWillMount() {
    
  },
    
  componentDidMount() {
    this.setState({
      treeData: this.props.treeData,
      selectingValue: this.props.value
    })
    
  },

  componentWillReceiveProps(nextProps) {
    
  },

  onSelect(selectedKeys) {
    'function' === typeof(this.props.onSelect) 
    ? 
    this.props.onSelect(selectedKeys) 
    : 
    this.props.onSelect ? console.log('onSelect is not a function') : ''

    this.setState({
      selectingValue: selectedKeys
    })
  },

  showModal() {
    this.setState({
      showTreeBox: true,
    })
  },

  hideModal() {
    this.setState({
      showTreeBox: false,
    })
  },

  handleOk() {
    'function' === typeof(this.props.onOk) ? this.props.onOk(this.state.selectingValue[0]) : ''
    this.setState({
      value: this.state.selectingValue,
      selectingValue: undefined
    })
    this.hideModal();
  },

  handleCancel() {
    this.setState({
      selectingValue: undefined
    })
    this.hideModal();
  },

  getValueById(id) {
    const treeData = this.state.treeData || [];
    const target = treeData.filter(item => {
      return item.id == id
    })
    const value = target.length > 0 ? target[0].name : '';
    return value;
  },

  // render() {
  //   const { multiple, checkable, treeProps, treeData, value, changeable, handleOk, handleCancel } = this.props;
  //   return (
  //     <div>
  //     {
  //       changeable ? 
  //       (<a
  //         href="javascript:;" 
  //         onClick={this.showModal}
  //         { ...treeProps }
  //       >
  //       { this.state.value ? this.getValueById(this.state.value) : this.getValueById(value) }
  //       </a>)
  //       :
  //       this.state.value ? this.getValueById(this.state.value) : this.getValueById(value)
  //     }
  //     <Modal 
  //     title="选择人员" 
  //     visible={ this.state.showTreeBox }
  //     onOk={ handleOk || this.handleOk } 
  //     onCancel={ handleCancel || this.handleCancel }
  //       >
  //       <MyTree
  //         treeData={treeData}
  //         multiple={ multiple }
  //         checkable={ checkable }
  //         defaultValue={ value }
  //         onSelect={ this.onSelect }
  //       />  
  //     </Modal>
  //   </div>
  // )
  // }
  render() {
    const selectAfter = (<a href="javascript:;"><Icon type="edit" onClick={this.showModal}/></a>)
    const { multiple, checkable, treeProps, treeData, changeable, handleOk, handleCancel } = this.props;
    const value = treeProps.value;
    return (
      <div>
      {
        changeable ? 
        (<Input 
          { ...treeProps }
          addonAfter={selectAfter} 
          value={this.state.value ? this.getValueById(this.state.value) : this.getValueById(value)} 
        />)
        :
        this.state.value ? this.getValueById(this.state.value) : this.getValueById(value)
      }
      <Modal 
      title="选择人员" 
      visible={ this.state.showTreeBox }
      onOk={ handleOk || this.handleOk } 
      onCancel={ handleCancel || this.handleCancel }
        >
        <div style={{padding: '20px'}}>
          <MyTree
            treeData={treeData}
            multiple={ multiple }
            checkable={ checkable }
            defaultValue={ value }
            onSelect={ this.onSelect }
          /> 
        </div>   
      </Modal>
    </div>
  )
  }
})

TreeBox.propTypes = {
  tree: PropTypes.object,
}

TreeBox.defaultProps = {
  tree: {},
}


export { 
  TreeBox as default,
  AsynTreeBox,
  MyTree
}

