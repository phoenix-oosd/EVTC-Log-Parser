using System.Collections.Generic;

namespace EVTC_Log_Parser.Model
{
    public abstract class BoonStack
    {
        #region Members
        protected readonly int _capacity;
        protected List<int> _stack = new List<int>();
        #endregion

        #region Properties
        public List<int> Stack
        {
            get { return _stack; }
            set { _stack = value; }
        }
        #endregion

        #region Constructor
        public BoonStack(int capacity)
        {
            _capacity = capacity;
        }
        #endregion

        #region Abstract Methods
        public abstract int CalculateValue();
        public abstract void Update(int timePassed);
        #endregion

        #region Public Methods
        public void Add(int duration)
        {
            if (IsFull())
            {
                int i = _stack.Count - 1;
                if (_stack[i] < duration)
                {
                    _stack[i] = duration;
                    ReverseSort();
                }
            }
            else
            {
                _stack.Add(duration);
                ReverseSort();
            }
        }
        #endregion

        #region Protected Methods
        protected bool IsFull()
        {
            return _stack.Count >= _capacity;
        }

        protected void ReverseSort()
        {
            _stack.Sort();
            _stack.Reverse();
        }
        #endregion
    }
}
