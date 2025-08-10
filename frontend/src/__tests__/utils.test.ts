// Utility function tests for e-commerce application
describe('Utility Functions', () => {
  describe('Date and Time Utilities', () => {
    it('should format date correctly', () => {
      const testDate = new Date('2023-12-25T10:30:00Z');
      const formattedDate = testDate.toLocaleDateString('zh-CN');
      
      expect(typeof formattedDate).toBe('string');
      expect(formattedDate).toMatch(/\d{4}\/\d{1,2}\/\d{1,2}/);
    });

    it('should calculate time difference', () => {
      const startTime = new Date('2023-12-25T10:00:00Z');
      const endTime = new Date('2023-12-25T11:30:00Z');
      const diffInMinutes = (endTime.getTime() - startTime.getTime()) / (1000 * 60);
      
      expect(diffInMinutes).toBe(90);
    });
  });

  describe('String Utilities', () => {
    it('should capitalize first letter', () => {
      const capitalize = (str: string) => str.charAt(0).toUpperCase() + str.slice(1);
      
      expect(capitalize('hello')).toBe('Hello');
      expect(capitalize('world')).toBe('World');
      expect(capitalize('')).toBe('');
    });

    it('should truncate long strings', () => {
      const truncate = (str: string, maxLength: number) => 
        str.length > maxLength ? str.slice(0, maxLength) + '...' : str;
      
      expect(truncate('Hello World', 5)).toBe('Hello...');
      expect(truncate('Short', 10)).toBe('Short');
      expect(truncate('', 5)).toBe('');
    });

    it('should format currency', () => {
      const formatCurrency = (amount: number, currency: string = 'CNY') => {
        return new Intl.NumberFormat('zh-CN', {
          style: 'currency',
          currency,
        }).format(amount);
      };
      
      expect(formatCurrency(1234.56)).toBe('¥1,234.56');
      expect(formatCurrency(0)).toBe('¥0.00');
      expect(formatCurrency(999999.99)).toBe('¥999,999.99');
    });
  });

  describe('Number Utilities', () => {
    it('should round numbers correctly', () => {
      expect(Math.round(3.4)).toBe(3);
      expect(Math.round(3.5)).toBe(4);
      expect(Math.round(3.6)).toBe(4);
      expect(Math.round(-3.4)).toBe(-3);
      expect(Math.round(-3.5)).toBe(-3);
    });

    it('should format large numbers', () => {
      const formatNumber = (num: number) => {
        if (num >= 1000000) {
          return (num / 1000000).toFixed(1) + 'M';
        }
        if (num >= 1000) {
          return (num / 1000).toFixed(1) + 'K';
        }
        return num.toString();
      };
      
      expect(formatNumber(1234)).toBe('1.2K');
      expect(formatNumber(1234567)).toBe('1.2M');
      expect(formatNumber(999)).toBe('999');
    });

    it('should validate numeric ranges', () => {
      const isInRange = (value: number, min: number, max: number) => 
        value >= min && value <= max;
      
      expect(isInRange(5, 1, 10)).toBe(true);
      expect(isInRange(1, 1, 10)).toBe(true);
      expect(isInRange(10, 1, 10)).toBe(true);
      expect(isInRange(0, 1, 10)).toBe(false);
      expect(isInRange(11, 1, 10)).toBe(false);
    });
  });

  describe('Array Utilities', () => {
    it('should remove duplicates from array', () => {
      const removeDuplicates = <T>(arr: T[]): T[] => Array.from(new Set(arr));
      
      expect(removeDuplicates([1, 2, 2, 3, 3, 4])).toEqual([1, 2, 3, 4]);
      expect(removeDuplicates(['a', 'b', 'a', 'c'])).toEqual(['a', 'b', 'c']);
      expect(removeDuplicates([])).toEqual([]);
    });

    it('should group array items by key', () => {
      const groupBy = <T>(arr: T[], key: keyof T) => {
        return arr.reduce((groups, item) => {
          const groupKey = String(item[key]);
          if (!groups[groupKey]) {
            groups[groupKey] = [];
          }
          groups[groupKey].push(item);
          return groups;
        }, {} as Record<string, T[]>);
      };
      
      const items = [
        { id: 1, category: 'A', name: 'Item 1' },
        { id: 2, category: 'B', name: 'Item 2' },
        { id: 3, category: 'A', name: 'Item 3' },
      ];
      
      const result = groupBy(items, 'category');
      expect(result.A).toHaveLength(2);
      expect(result.B).toHaveLength(1);
    });

    it('should sort array by multiple criteria', () => {
      const sortByMultiple = <T>(arr: T[], ...criteria: Array<keyof T>) => {
        return [...arr].sort((a, b) => {
          for (const criterion of criteria) {
            if (a[criterion] < b[criterion]) return -1;
            if (a[criterion] > b[criterion]) return 1;
          }
          return 0;
        });
      };
      
      const items = [
        { name: 'Alice', age: 30, score: 85 },
        { name: 'Bob', age: 25, score: 90 },
        { name: 'Charlie', age: 25, score: 80 },
      ];
      
      const result = sortByMultiple(items, 'age', 'score');
      expect(result[0].name).toBe('Charlie');
      expect(result[1].name).toBe('Bob');
      expect(result[2].name).toBe('Alice');
    });
  });

  describe('Object Utilities', () => {
    it('should deep clone objects', () => {
      const deepClone = <T>(obj: T): T => JSON.parse(JSON.stringify(obj));
      
      const original = { a: 1, b: { c: 2, d: [3, 4] } };
      const cloned = deepClone(original);
      
      expect(cloned).toEqual(original);
      expect(cloned).not.toBe(original);
      expect(cloned.b).not.toBe(original.b);
    });

    it('should merge objects deeply', () => {
      const deepMerge = (target: any, source: any) => {
        const result = { ...target };
        for (const key in source) {
          if (source[key] && typeof source[key] === 'object' && !Array.isArray(source[key])) {
            result[key] = deepMerge(result[key] || {}, source[key]);
          } else {
            result[key] = source[key];
          }
        }
        return result;
      };
      
      const target = { a: 1, b: { c: 2, d: 3 } };
      const source = { b: { d: 4, e: 5 }, f: 6 };
      const result = deepMerge(target, source);
      
      expect(result).toEqual({
        a: 1,
        b: { c: 2, d: 4, e: 5 },
        f: 6,
      });
    });

    it('should pick specific properties from object', () => {
      const pick = <T extends object, K extends keyof T>(obj: T, keys: K[]): Pick<T, K> => {
        const result = {} as Pick<T, K>;
        keys.forEach(key => {
          if (key in obj) {
            result[key] = obj[key];
          }
        });
        return result;
      };
      
      const obj = { a: 1, b: 2, c: 3, d: 4 };
      const result = pick(obj, ['a', 'c']);
      
      expect(result).toEqual({ a: 1, c: 3 });
      expect(Object.keys(result)).toEqual(['a', 'c']);
    });
  });

  describe('Validation Utilities', () => {
    it('should validate email format', () => {
      const isValidEmail = (email: string): boolean => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
      };
      
      expect(isValidEmail('test@example.com')).toBe(true);
      expect(isValidEmail('user.name@domain.co.uk')).toBe(true);
      expect(isValidEmail('invalid-email')).toBe(false);
      expect(isValidEmail('test@')).toBe(false);
      expect(isValidEmail('@example.com')).toBe(false);
      expect(isValidEmail('')).toBe(false);
    });

    it('should validate phone number format', () => {
      const isValidPhone = (phone: string): boolean => {
        const phoneRegex = /^1[3-9]\d{9}$/;
        return phoneRegex.test(phone);
      };
      
      expect(isValidPhone('13800138000')).toBe(true);
      expect(isValidPhone('18612345678')).toBe(true);
      expect(isValidPhone('12345678901')).toBe(false);
      expect(isValidPhone('1380013800')).toBe(false);
      expect(isValidPhone('abc12345678')).toBe(false);
    });

    it('should validate required fields', () => {
      const validateRequired = (value: any): boolean => {
        if (value === null || value === undefined) return false;
        if (typeof value === 'string') return value.trim().length > 0;
        if (Array.isArray(value)) return value.length > 0;
        return true;
      };
      
      expect(validateRequired('hello')).toBe(true);
      expect(validateRequired('  ')).toBe(false);
      expect(validateRequired('')).toBe(false);
      expect(validateRequired(null)).toBe(false);
      expect(validateRequired(undefined)).toBe(false);
      expect(validateRequired([1, 2, 3])).toBe(true);
      expect(validateRequired([])).toBe(false);
      expect(validateRequired(0)).toBe(true);
      expect(validateRequired(false)).toBe(true);
    });
  });

  describe('Async Utilities', () => {
    it('should debounce function calls', async () => {
      const debounce = <T extends (...args: any[]) => any>(
        func: T,
        delay: number
      ): ((...args: Parameters<T>) => void) => {
        let timeoutId: NodeJS.Timeout;
        return (...args: Parameters<T>) => {
          clearTimeout(timeoutId);
          timeoutId = setTimeout(() => func(...args), delay);
        };
      };
      
      let callCount = 0;
      const debouncedFn = debounce(() => { callCount++; }, 100);
      
      debouncedFn();
      debouncedFn();
      debouncedFn();
      
      expect(callCount).toBe(0);
      
      await new Promise(resolve => setTimeout(resolve, 150));
      expect(callCount).toBe(1);
    });

    it('should throttle function calls', async () => {
      const throttle = <T extends (...args: any[]) => any>(
        func: T,
        delay: number
      ): ((...args: Parameters<T>) => void) => {
        let lastCall = 0;
        return (...args: Parameters<T>) => {
          const now = Date.now();
          if (now - lastCall >= delay) {
            lastCall = now;
            func(...args);
          }
        };
      };
      
      let callCount = 0;
      const throttledFn = throttle(() => { callCount++; }, 100);
      
      throttledFn();
      throttledFn();
      throttledFn();
      
      expect(callCount).toBe(1);
      
      await new Promise(resolve => setTimeout(resolve, 150));
      throttledFn();
      expect(callCount).toBe(2);
    });
  });
}); 