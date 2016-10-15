import React from 'react'
import { Form, Input, Select, TreeSelect, Button, Checkbox, Tag, Badge, Spin } from 'antd'
import Container from '../Container';
import Box from '../Box'

const FormItem = Form.Item;
const Option = Select.Option;
const OptGroup = Select.OptGroup;
const CheckboxGroup = Checkbox.Group;
const RoleDetail = ({roleDetail, type, groups=[], areaMap={}, zoneMap={}, permissionMap={}, submiting, dispatch, history, form, ...rest}) => {
  const { getFieldProps, getFieldError, isFieldValidating } = form;

  const formItemLayout = {
    labelCol: { span: 2 },
    wrapperCol: { span: 7 },
  };

  const optionGroups = Object.keys(permissionMap).map((key) => {
    const permissions = permissionMap[key];
    return {
      groupName: permissions[0].groupName,
      permissions: permissions.map((permission) => {
        return { label: permission.name, value: permission.id }
      })
    }
  })

  const nameProps = getFieldProps('name', {
    rules: [
      { required: true, min: 1, message: '角色名至少1个字符' }
    ]
  });

  const typeProps = getFieldProps('type', {
    rules: [
      { required: true, message: '必须选择一个角色类别' }
    ]
  })

  const permissionProps = getFieldProps('permissions', {
    rules: [
      { type: "array", required: true, min: 1, message: '角色必须要选中权限' },
    ]
  })

  const orgProps = getFieldProps('orgId', {})

  return (
    <Container
      {... rest}
    >
      <Spin spinning={ submiting }>
        <Box>
          <Form
            horizontal
            onSubmit={
              (e) => { 
                e.preventDefault();
                const newRoleDetail = form.getFieldsValue();
                dispatch({ 
                  type: `role/${type}`, 
                  payload: {
                    roleDetail: newRoleDetail, 
                    callback: (result) => {
                      if(!result.err) {
                        history.goBack();
                      }
                    }
                  }
                }) 
              }
            }
          >
            <FormItem
              label="角色名称"
              required={ true }
              { ...formItemLayout }
              hasFeedback
            >
              <Input { ...nameProps } />
              {
                type === 'update'?
                <a>查看角色下的用户</a>
                :
                undefined
              }
            </FormItem>
            <FormItem
              label="角色类别"
              required={ true }
              { ...formItemLayout }
            >
              <Select { ...typeProps } onChange={(value) => {
                  switch(value) {
                    case '1': 
                      dispatch({ type: 'group/query' });
                      break;
                    case '2':
                      dispatch({ type: 'area/query' });
                      break;
                    case '3':
                      dispatch({ type: 'zone/query' });
                      break;
                  }
                  const newRoleDetail = form.getFieldsValue();
                  newRoleDetail.type = value;
                  newRoleDetail.orgId = undefined;
                  dispatch({ type: 'role/editDetail', payload: { ...newRoleDetail } })
                }}>
                <Option value="1">集团</Option>
                <Option value="2">区域</Option>
                <Option value="3">园区</Option>
                <Option value="4">所有园区通用角色</Option>
              </Select>
            </FormItem>
            {
              typeProps.value != '4'?
              (
                <FormItem
                  label="所属组织"
                  required={ true }
                  { ...formItemLayout }
                >
                  {
                    typeProps.value?
                    (
                      <Select { ...orgProps } >
                        {
                          typeProps.value === '1' && groups?
                          groups.map((group, index) => (
                            <Option value={`${group.id}`} key={index}>{group.name}</Option>
                          ))
                          :
                          typeProps.value === '2'?
                          Object.keys(areaMap).map((groupId, index) => (
                            <OptGroup label={areaMap[groupId].groupName} key={index}>
                              {
                                areaMap[groupId].areas.map((area, areaIndex) => (
                                  <Option value={`${area.id}`} key={areaIndex}>{area.name}</Option>
                                ))
                              }
                            </OptGroup>
                          ))
                          :
                          typeProps.value === '3'?
                          Object.keys(zoneMap).map((areaId, index) => (
                            <OptGroup label={`${zoneMap[areaId].groupName}-${zoneMap[areaId].areaName}`} key={index}>
                              {
                                zoneMap[areaId].zones.map((zone, zoneIndex) => (
                                  <Option value={`${zone.id}`} key={zoneIndex}>{zone.name}</Option>
                                ))
                              }
                            </OptGroup>
                          ))
                          :
                          undefined
                        }
                      </Select>
                    )
                    :
                    <span>请先选择角色类别</span>
                  }
                </FormItem>
              )
              :
              undefined
            }
            
            <FormItem
              label="权限"
              required={ true }
              { ...formItemLayout }
              wrapperCol={{ span: 7 }}
              hasFeedback
            >
              {
                optionGroups.map((optionGroup, index) => ( 
                  <div key={index}>
                    <Tag>{optionGroup.groupName}</Tag>
                    <CheckboxGroup 
                      options={optionGroup.permissions}
                      { ...permissionProps }
                    />
                    <hr />
                  </div>
                ))
              }
            </FormItem>
            <FormItem wrapperCol={{ span: 7, offset: 2 }}>
              <Button type="primary" htmlType="submit">确定</Button>
              &nbsp;
              <Button type="primary" onClick={() => { history.goBack() }}>返回</Button>
            </FormItem>
          </Form>
        </Box>
      </Spin>
    </Container>
  )
}

export default 
Form.create(
      {
        mapPropsToFields: function ({dispatch, roleDetail, groups, areaMap, zoneMap}) {
          let formData = {};
          Object.keys(roleDetail).forEach((key) => {
            if( roleDetail[key] instanceof Array ) {
              formData[key] = { value: roleDetail[key] }
            } else {
              if(roleDetail[key]){
                formData[key] = { value: `${roleDetail[key]}` };
              }
            }
          })

          const type = `${roleDetail.type}`;
          if(type && type !== '4' ) {
            if(type === '1' && groups.length == 0) {
              dispatch({ type: 'group/query' });
            } else if(type === '2' && !areaMap) {
              dispatch({ type: 'area/query' });
            } else if(type === '3' && !zoneMap) {
              dispatch({ type: 'zone/query' });
            }
          }
          
          return formData;
        }
      }
)(RoleDetail)
