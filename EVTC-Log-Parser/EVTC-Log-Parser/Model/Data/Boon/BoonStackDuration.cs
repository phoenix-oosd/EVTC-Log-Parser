using System;
using System.Linq;

namespace EVTC_Log_Parser.Model
{
    public class BoonStackDuration : BoonStack
    {
        #region Constructor
        public BoonStackDuration(int capacity) : base(capacity) { }
        #endregion

        #region Abstract Methods
        public override int CalculateValue()
        {
            return _stack.Sum();
        }

        public override void Update(int timePassed)
        {
            if (_stack.Count > 0)
            {
                if (timePassed >= CalculateValue())
                {
                    _stack.Clear();
                }
                else
                {
                    _stack[0] -= timePassed;
                    if (_stack[0] < 0)
                    {
                        timePassed = _stack[0];
                        _stack.RemoveAt(0);
                        Update(Math.Abs(timePassed));
                    }
                }
            }
        }
        #endregion
    }
}
