// Mock antd components for testing
export const message = {
  success: jest.fn(),
  error: jest.fn(),
  warning: jest.fn(),
  info: jest.fn(),
  loading: jest.fn(),
};

export const Alert = jest.fn(({ message, type, onClose }: any) => {
  const element = document.createElement('div');
  element.setAttribute('data-testid', 'alert');
  element.className = `alert alert-${type}`;
  element.textContent = message;
  return element;
});

export const Button = jest.fn(({ children, onClick, ...props }: any) => {
  const element = document.createElement('button');
  element.onclick = onClick;
  element.textContent = children;
  return element;
});

export const Input = jest.fn(({ value, onChange, ...props }: any) => {
  const element = document.createElement('input');
  element.value = value;
  element.onchange = onChange;
  return element;
});

export const Table = jest.fn(({ dataSource, columns, ...props }: any) => {
  const element = document.createElement('table');
  return element;
}); 