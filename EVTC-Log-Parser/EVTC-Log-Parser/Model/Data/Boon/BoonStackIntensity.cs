using System;
using System.Collections.Generic;
using System.Linq;

namespace EVTC_Log_Parser.Model
{
    public class BoonStackIntensity : BoonStack
    {
        #region Constructor
        public BoonStackIntensity(int capacity) : base(capacity) { }
        #endregion

        #region Abstract Methods
        public override int CalculateValue()
        {
            return _stack.Count();
        }

        public override void Update(int timePassed)
        {
            for (int i = 0; i < _stack.Count; i++)
            {
                _stack[i] -= timePassed;
            }
            _stack.RemoveAll(i => i <= 0);
        }
        #endregion

        public void SimulateTimePassed(int totalTimePassed, List<int> timeLine)
        {
            BoonStack b = new BoonStackIntensity(_capacity);
            List<int> s = new List<int>(_stack);
            b.Stack = s;

            if (s.Count > 0)
            {
                int tt = 0;
                int min = s.Min();

                for (int i = 1; i < totalTimePassed; i++)
                {
                    if ((i - tt) >= min)
                    {
                        b.Update(i - tt);
                        if (s.Count > 0)
                        {
                            min = s.Min();
                        }
                        tt = i;
                    }
                    timeLine.Add(b.CalculateValue());
                }
            }
            else
            {
                for (int i = 1; i < totalTimePassed; i++)
                {
                    timeLine.Add(0);
                }
            }
        }
    }
}
