import React, { useEffect, useState } from 'react';
import { 
  Table, 
  Button, 
  Input, 
  Space, 
  Modal, 
  Form, 
  InputNumber, 
  Switch, 
  message, 
  Popconfirm,
  Card,
  Row,
  Col,
  Typography
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined, EyeOutlined, EyeInvisibleOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { 
  fetchProducts, 
  createProduct, 
  updateProduct, 
  deleteProduct, 
  toggleProductVisibility,
  setFilter,
  setPagination,
  resetRefreshFlag
} from '../../store/slices/productsSlice';
import { Product, ProductFormData } from '../../types';
import styles from './index.module.css';

const { Title } = Typography;
const { Search } = Input;

const AdminPage: React.FC = () => {
  const dispatch = useDispatch();
  const { products, loading, pagination, filter, shouldRefresh } = useSelector((state: RootState) => state.products);
  
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [form] = Form.useForm();

  // Fetch product list - when dependencies change
  useEffect(() => {
    // Always fetch when filter, pagination, or shouldRefresh changes
    dispatch(fetchProducts({ 
      pageNum: pagination.pageNum, 
      pageSize: pagination.pageSize, 
      filter 
    }) as any);
    
    // Reset refresh flag after fetching
    if (shouldRefresh) {
      dispatch(resetRefreshFlag());
    }
  }, [dispatch, pagination.pageNum, pagination.pageSize, filter, shouldRefresh]);

  // Handle search
  const handleSearch = (value: string) => {
    dispatch(setFilter({ name: value }));
    // Fetch data immediately after search, as setFilter triggers useEffect
  };

  // Handle table changes
  const handleTableChange = (pagination: any) => {
    if (pagination.current !== undefined) {
      dispatch(setPagination({ 
        pageNum: pagination.current, 
        pageSize: pagination.pageSize || 10 
      }));
    }
  };

  // Open create/edit modal
  const showModal = (product?: Product) => {
    setEditingProduct(product || null);
    if (product) {
      form.setFieldsValue(product);
    } else {
      form.resetFields();
    }
    setIsModalVisible(true);
  };

  // Close modal
  const handleCancel = () => {
    setIsModalVisible(false);
    setEditingProduct(null);
    form.resetFields();
  };

  // Submit form
  const handleSubmit = async (values: ProductFormData) => {
    try {
      if (editingProduct) {
        const resultAction = await dispatch(updateProduct({ id: editingProduct.id, productData: values }) as any);
        if (updateProduct.fulfilled.match(resultAction)) {
          message.success('Product updated successfully');
          handleCancel();
        }
      } else {
        const resultAction = await dispatch(createProduct(values) as any);
        if (createProduct.fulfilled.match(resultAction)) {
          message.success('Product created successfully');
          handleCancel();
        }
      }
    } catch (error) {
      // Error handled by global interceptor, no additional handling needed here
      console.error('Operation failed:', error);
    }
  };

  // Delete product
  const handleDelete = async (id: number) => {
    try {
      const resultAction = await dispatch(deleteProduct(id) as any);
      if (deleteProduct.fulfilled.match(resultAction)) {
        message.success('Product deleted successfully');
      }
    } catch (error) {
      // Error handled by global interceptor
      console.error('Delete failed:', error);
    }
  };

  // Toggle product visibility
  const handleToggleVisibility = async (id: number, visible: boolean) => {
    try {
      const resultAction = await dispatch(toggleProductVisibility({ id, visible }) as any);
      if (toggleProductVisibility.fulfilled.match(resultAction)) {
        message.success(`Product ${visible ? 'shown' : 'hidden'} successfully`);
      }
    } catch (error) {
      // Error handled by global interceptor
      console.error('Operation failed:', error);
    }
  };

  // Table columns configuration
  const columns = [
    {
      title: 'Product Name',
      dataIndex: 'name',
      key: 'name',
      width: window.innerWidth <= 768 ? 120 : '30%',
      ellipsis: true,
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      width: window.innerWidth <= 768 ? 80 : '15%',
      render: (price: number) => `¥${price.toFixed(2)}`,
    },
    {
      title: 'Stock',
      dataIndex: 'quantity',
      key: 'quantity',
      width: window.innerWidth <= 768 ? 60 : '15%',
    },
    {
      title: 'Visibility',
      dataIndex: 'visible',
      key: 'visible',
      width: window.innerWidth <= 768 ? 80 : '15%',
      render: (visible: boolean, record: Product) => (
        <Switch
          checked={visible}
          onChange={(checked) => handleToggleVisibility(record.id, checked)}
          checkedChildren={<EyeOutlined />}
          unCheckedChildren={<EyeInvisibleOutlined />}
        />
      ),
    },
    {
      title: 'Actions',
      key: 'action',
      width: window.innerWidth <= 768 ? 120 : '25%',
      render: (_: any, record: Product) => (
        <Space size={window.innerWidth <= 768 ? 'small' : 'middle'}>
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => showModal(record)}
            size={window.innerWidth <= 768 ? 'small' : 'middle'}
          >
            {window.innerWidth <= 768 ? 'Edit' : 'Edit'}
          </Button>
          <Popconfirm
            title="Are you sure you want to delete this product?"
            onConfirm={() => handleDelete(record.id)}
            okText="Confirm"
            cancelText="Cancel"
          >
            <Button
              type="link"
              danger
              icon={<DeleteOutlined />}
              size={window.innerWidth <= 768 ? 'small' : 'middle'}
            >
              {window.innerWidth <= 768 ? 'Delete' : 'Delete'}
          </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div className={styles.adminPage}>
      <Card>
        <Row 
          justify="space-between" 
          align="middle" 
          className={`${styles.headerRow} ${window.innerWidth <= 768 ? styles.headerRowMobile : ''}`}
        >
          <Col className={`${styles.titleCol} ${window.innerWidth <= 768 ? styles.titleColMobile : ''}`}>
            <Title level={window.innerWidth <= 768 ? 3 : 2} className={styles.pageTitle}>
              Product Management
            </Title>
          </Col>
          <Col className={`${styles.addButtonCol} ${window.innerWidth <= 768 ? styles.addButtonColMobile : ''}`}>
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => showModal()}
              size={window.innerWidth <= 768 ? 'middle' : 'large'}
              className={`${styles.addButton} ${window.innerWidth <= 768 ? styles.addButtonMobile : ''}`}
            >
              Add Product
            </Button>
          </Col>
        </Row>

        {/* Search bar */}
        <Row className={`${styles.searchRow} ${window.innerWidth <= 768 ? styles.searchRowMobile : ''}`}>
          <Col span={window.innerWidth <= 768 ? 24 : 8} className={`${styles.searchCol} ${window.innerWidth <= 768 ? styles.searchColMobile : ''}`}>
            <Search
              placeholder="Search product name"
              allowClear
              enterButton={<SearchOutlined />}
              onSearch={handleSearch}
              defaultValue={filter.name}
            />
          </Col>
        </Row>

        {/* Product table */}
        <Table
          columns={columns}
          dataSource={products}
          rowKey="id"
          loading={loading}
          scroll={{ x: 'max-content' }}
          size="small"
          pagination={{
            current: pagination.pageNum,
            pageSize: window.innerWidth <= 768 ? 10 : pagination.pageSize,
            total: pagination.total,
            showSizeChanger: window.innerWidth > 768,
            showQuickJumper: window.innerWidth > 768,
            showTotal: (total, range) => `Page ${range[0]}-${range[1]} of ${total}`,
            size: window.innerWidth <= 768 ? 'small' : 'default',
          }}
          onChange={handleTableChange}
        />
      </Card>

      {/* Create/Edit product modal */}
      <Modal
        title={editingProduct ? 'Edit' : 'Add'}
        open={isModalVisible}
        onCancel={handleCancel}
        footer={null}
        width={window.innerWidth <= 768 ? '90%' : 600}
        className={`${styles.modal} ${window.innerWidth <= 768 ? styles.modalMobile : ''}`}
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          initialValues={{ visible: true }}
        >
          <Form.Item
            name="name"
            label="Product Name"
            rules={[{ required: true, message: 'Please enter product name' }]}
          >
            <Input placeholder="Enter product name" />
          </Form.Item>

          <Form.Item
            name="price"
            label="Price"
            rules={[{ required: true, message: 'Please enter price' }]}
          >
            <InputNumber
              placeholder="Enter price"
              min={0}
              precision={2}
              className={styles.formItem}
            />
          </Form.Item>

          <Form.Item
            name="quantity"
            label="Stock"
            rules={[{ required: true, message: 'Please enter stock quantity' }]}
          >
            <InputNumber
              placeholder="Enter stock quantity"
              min={0}
              className={styles.formItem}
            />
          </Form.Item>

          <Form.Item
            name="visible"
            label="Visibility"
            valuePropName="checked"
          >
            <Switch checkedChildren="Visible" unCheckedChildren="Hidden" />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button type="primary" htmlType="submit" loading={loading}>
                {editingProduct ? 'Update' : 'Create'}
              </Button>
              <Button onClick={handleCancel}>
                Cancel
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default AdminPage; 