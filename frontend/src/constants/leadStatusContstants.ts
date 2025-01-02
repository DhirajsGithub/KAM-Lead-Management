export const LEAD_STATUS = {
    NEW: 'NEW',
    CONTACTED: 'CONTACTED',
    QUALIFIED: 'QUALIFIED',
    NEGOTIATING: 'NEGOTIATING',
    CONVERTED: 'CONVERTED',
    LOST: 'LOST'
  } as const;
  
  export const LEAD_STATUS_COLORS = {
    [LEAD_STATUS.NEW]: '#e6f4ff',
    [LEAD_STATUS.CONTACTED]: '#f6ffed',
    [LEAD_STATUS.QUALIFIED]: '#fff7e6',
    [LEAD_STATUS.NEGOTIATING]: '#f4f0ff',
    [LEAD_STATUS.CONVERTED]: '#e6fffb',
    [LEAD_STATUS.LOST]: '#fff1f0'
  } as const;
  
  export const LEAD_STATUS_TEXT_COLORS = {
    [LEAD_STATUS.NEW]: '#1677ff',
    [LEAD_STATUS.CONTACTED]: '#52c41a',
    [LEAD_STATUS.QUALIFIED]: '#faad14',
    [LEAD_STATUS.NEGOTIATING]: '#722ed1',
    [LEAD_STATUS.CONVERTED]: '#13c2c2',
    [LEAD_STATUS.LOST]: '#f5222d'
  } as const;