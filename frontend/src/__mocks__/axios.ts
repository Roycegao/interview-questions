// Mock axios for testing
const mockAxios: any = {
  create: jest.fn(() => mockAxios),
  get: jest.fn(),
  post: jest.fn(),
  put: jest.fn(),
  patch: jest.fn(),
  delete: jest.fn(),
  interceptors: {
    request: {
      use: jest.fn(),
      eject: jest.fn(),
    },
    response: {
      use: jest.fn(),
      eject: jest.fn(),
    },
  },
  defaults: {
    baseURL: '',
    headers: {},
  },
};

export default mockAxios;
export const AxiosInstance = jest.fn();
export const AxiosResponse = jest.fn();
export const AxiosError = jest.fn();
export const isAxiosError = jest.fn(); 